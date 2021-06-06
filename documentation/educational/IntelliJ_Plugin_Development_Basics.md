# IntelliJ Plugin Development Basics
This is a document we created during the development of the TestBuddy IntelliJ Plugin to help ourselves and future developers quickly get an idea of what it means to develop a plugin. It is heavily based on the official [IntelliJ Platform Plugin SDK](https://plugins.jetbrains.com/docs/intellij/welcome.html).

## Actions 
[Action Docs](https://plugins.jetbrains.com/docs/intellij/plugin-actions.html)

[Action System Docs](https://plugins.jetbrains.com/docs/intellij/basic-action-system.html#action-implementation)

[Creating Actions Docs](https://plugins.jetbrains.com/docs/intellij/working-with-custom-actions.html)

An actions is a class derived from the `AnAction` class. Every action needs to override the `actionPerformed()` method which is called every time the action is performed by selcting its menu item/toolbar button, using its shortcut or invoking it through **Help | Find Action**.

### Principal Implementation Overrides
Actions need to override 2 methods of the `AnAction` class.

* `AnAction.actionPerformed(event: AnActionEvent)`: This method is called every time the action is invoked. It takes an [`AnActionEvent`](https://upsource.jetbrains.com/idea-ce/file/idea-ce-b8f393732fae6beb1e4f70ec4d605f2838253529/platform/editor-ui-api/src/com/intellij/openapi/actionSystem/AnActionEvent.java) object as a parameter that containts information about the context at which the action is invoked. The `AnActionEvent` object can be used to access projects, files, selection etc. 
    * To override it: Here is an [example](https://github.com/JetBrains/intellij-sdk-code-samples/blob/main/action_basics/src/main/java/org/intellij/sdk/action/PopupDialogAction.java)

* `AnAction.update(event: AnActionEvent)`: The `update` method is called by the IntelliJ Platform to update the action's state (`visible`, `enabled`). Because the `update` method is periodically called by the IntelliJ platform in response to user gestures, it is important that it executes **very quickly**.
    * To override it: First of all here is an [example](https://github.com/JetBrains/intellij-sdk-code-samples/blob/main/action_basics/src/main/java/org/intellij/sdk/action/PopupDialogAction.java). The action's enable/disable state and visibility are set using the methods of the `Presentation` object, which can be accessed by using `AnActionEvent.getPresentation()`. Then the `Presentation` object has the `Presentation.setEnabled()` and `Presentation.setVisible()` methods. If the action is enabled then the `actionPerformed()` method will be called when the action is invoked. 

### The AnActionEvent object
From the action event object we have access to the `Presentation` and whether the action was invoked by a toolbar. `AnActionEvent.getData()` returns a [`CommonDataKeys`](https://plugins.jetbrains.com/docs/intellij/basic-action-system.html#overriding-the-anactionactionperformed-method) object that gives us access to the `Project`, `Editor`, `PsiFile` and more. 


### Registering Actions

#### Registering actions in plugin.xml

You register actions inside the `<actions>` section of the `plugin.xml` file. Inside this section use the `<action>...</action>` tags to declare a single action. In the `<action>` tag you can define the `<override-text place="..." text="...">`
element to give alternate names depending on where the action appears. Here is an example:
```xml=
<actions>
  <!-- The <action> element defines an action to register.
       The mandatory "id" attribute specifies a unique
       identifier for the action.
       The mandatory "class" attribute specifies the
       FQN of the class implementing the action.
       The mandatory "text" attribute specifies the default long-version text to be displayed for the
       action (tooltip for toolbar button or text for menu item).
       The optional "use-shortcut-of" attribute specifies the ID
       of the action whose keyboard shortcut this action will use.
       The optional "description" attribute specifies the text
       which is displayed in the status bar when the action is focused.
       The optional "icon" attribute specifies the icon which is
       displayed on the toolbar button or next to the menu item. -->
  <action id="VssIntegration.GarbageCollection" class="com.foo.impl.CollectGarbage" text="Garbage Collector: Collect _Garbage"
                description="Run garbage collector" icon="icons/garbage.png">
    <!-- The <override-text> element defines an alternate version of the text for the menu action.
         The mandatory "text" attribute defines the text to be displayed for the action.
         The mandatory "place" attribute declares where the alternate text should be used. In this example,
         any time the action is displayed in the IDE Main Menu (and submenus) the override-text
         version should be used.
         The second <override-text> element uses the alternate attribute "use-text-of-place" to define
         a location (EditorPopup) to use the same text as is used in MainMenu. It is a way to specify
         use of alternate menu text in multiple discrete menu groups. -->
    <override-text place="MainMenu" text="Collect _Garbage"/>
    <override-text place="EditorPopup" use-text-of-place="MainMenu"/>
    <!-- Provide alternative names for searching action by name -->
    <synonym text="GC"/>
    <!-- The <add-to-group> node specifies that the action should be added
         to an existing group. An action can be added to several groups.
         The mandatory "group-id" attribute specifies the ID of the group
         to which the action is added.
         The group must be implemented by an instance of the DefaultActionGroup class.
         The mandatory "anchor" attribute specifies the position of the
         action in the relative to other actions. It can have the values
         "first", "last", "before" and "after".
         The "relative-to-action" attribute is mandatory if the anchor
         is set to "before" and "after", and specifies the action before or after which
         the current action is inserted. -->
    <add-to-group group-id="ToolsMenu" relative-to-action="GenerateJavadoc" anchor="after"/>
      <!-- The <keyboard-shortcut> node specifies the keyboard shortcut
           for the action. An action can have several keyboard shortcuts.
           The mandatory "first-keystroke" attribute specifies the first
           keystroke of the action. The keystrokes are specified according
           to the regular Swing rules.
           The optional "second-keystroke" attribute specifies the second
           keystroke of the action.
           The mandatory "keymap" attribute specifies the keymap for which
           the action is active. IDs of the standard keymaps are defined as
           constants in the com.intellij.openapi.keymap.KeymapManager class.
           The optional "remove" attribute in the second <keyboard-shortcut>
           element below means the specified shortcut should be removed from
           the specified action.
           The optional "replace-all" attribute in the third <keyboard-shortcut>
           element below means remove all keyboard and mouse shortcuts from the specified
           action before adding the specified shortcut.  -->
    <!-- Add the first and second keystrokes to all keymaps  -->
    <keyboard-shortcut keymap="$default" first-keystroke="control alt G" second-keystroke="C"/>
    <!-- Except to the "Mac OS X" keymap and its children -->
    <keyboard-shortcut keymap="Mac OS X" first-keystroke="control alt G" second-keystroke="C" remove="true"/>
    <!-- The "Mac OS X 10.5+" keymap and its children will have only this keyboard shortcut for this action.  -->
    <keyboard-shortcut keymap="Mac OS X 10.5+" first-keystroke="control alt G" second-keystroke="C" replace-all="true"/>
    <!-- The <mouse-shortcut> node specifies the mouse shortcut for the
           action. An action can have several mouse shortcuts.
           The mandatory "keystroke" attribute specifies the clicks and
           modifiers for the action. It is defined as a sequence of words
           separated by spaces:
           "button1", "button2", "button3" for the mouse buttons;
           "shift", "control", "meta", "alt", "altGraph" for the modifier keys;
           "doubleClick" if the action is activated by a double-click of the button.
           The mandatory "keymap" attribute specifies the keymap for which
           the action is active. IDs of the standard keymaps are defined as
           constants in the com.intellij.openapi.keymap.KeymapManager class.
           The "remove" and "replace-all" attributes can also be used in
           a <mouse-shortcut> element. See <keyboard-shortcut> for documentation.  -->
    <mouse-shortcut keymap="$default" keystroke="control button3 doubleClick"/>
  </action>
```

From 2020.3 and on you can also define [synonyms](https://plugins.jetbrains.com/docs/intellij/basic-action-system.html#registering-actions) for your actions for the users to find using **Help | Find Action**:

```xml=
<action id="MyAction" text="My Action Name" ...>
  <synonym text="Another Search Term"/>
</action>
```
#### Registering actions in code
To register an action in code you need to pass an instance of its class to `ActionManager.registerAction()`. That will associate the action with an ID. You also need to add the action to at least one group. 

#### Building UI from Actions
Check out [the docs](https://plugins.jetbrains.com/docs/intellij/basic-action-system.html#building-ui-from-actions)

### Creating Actions
In [this section of the SDK docs](https://plugins.jetbrains.com/docs/intellij/working-with-custom-actions.html#creating-a-custom-action) an example of a custom action written in Java is given. Here we give the equivalent example in Kotlin. 

#### Implementing the action
In a file called `PopupDialogAction.kt` we have the following code: 
```kotlin=
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
pro
import com.intellij.openapi.ui.Messages
import com.intellij.pom.Navigatable

class PopupDialogAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        // Using the event, evaluate the context, and enable or disable the action
        val currentProject : Project? = event.project
        val nav : Navigatable? = event.getData(CommonDataKeys.NAVIGATABLE)
        val dlgMsg : String = if (nav != null) "${event.presentation.text} Selected! \nSelected Element ${nav.toString()}" else "${event.presentation.text} Selected!"
        val dlgTitle : String = event.presentation.description
        Messages.showMessageDialog(currentProject, dlgMsg, dlgTitle, Messages.getInformationIcon())
    }

    override fun update(event: AnActionEvent) {
        // Using the event, implement an action. For example, create and show a dialog.
        val project: Project? = event.project
        event.presentation.isEnabledAndVisible = (project != null)
    }

}
```

#### Registering the action
When writing the above code in IntelliJ you will get a warning about the action not being registered. You can use the suggested actions to resolve the issue which will prompt you with a GUI editor to add info for the plugin. If you prefer to do things the old fashioned way you can put the following in the `plugin.xml` file (inside the `<idea-plugin>` tag):
```xml=
    <actions>
        <action id="com.testbuddy.actions.PopupDialogAction" class="com.testbuddy.actions.PopupDialogAction"
                text="Pop Dialog Action" description="SDK action example">
            <add-to-group group-id="ToolsMenu" anchor="first"/>
        </action>
    </actions>
```

## Listeners
Listeners allow plugins to declaratively subscribe to events delivered through the [message bus](https://plugins.jetbrains.com/docs/intellij/messaging-infrastructure.html). There are 2 types of listeners: *application level* listeners and *project level* listeners.

You should declaratively register listeners, i.e declare them in the `plugin.xml` file. This way you will achieve better performance because listeners will be created lazily, meaning the listeners will be created only if the event that they subscribe to is fired.


## Services 

Services are the classes implementing the main logic of the plugin. They are called by the actions and the listeners and by other services as well. 
Something interesting to note is that Services are meant to be used as singletons. However, their "single nature" is not enforced by their own class
but instead by an instance manager. Because of that you should get Service instances in the following way: 

```kt
// project/application can be obtained in several different ways. To get the current project/app do:
val project = ProjectManager.getInstance().defaultProject
val application = ApplicationManager.getApplication()

val projectService = project.service<LoadTestsService>()
val applicationService = application.getService(SettingsService::class.java)
```

### Application vs Project Services
As you might have noticed in the above code snippet there are different kinds of services. They differ in their "scope". 
The "scope" determines how many instances of the service are created and managed by the instance manager. For project services,
a service instance will be created for each project. On the other hand, for application services one service instance will be made 
for the entire application. This means that if the service has state, then this state is shared over all projects that the user
opens in IntelliJ.

### Declaring Services
Similar to actions, services need to be declared in the `plugin.xml` file. You can do so in the following way.
```xml
<extensions defaultExtensionNs="com.intellij">

    <!-- Example project service -->
    <projectService serviceImplementation="com.testbuddy.services.LoadTestsService"/>

    <!-- Example application service -->
    <applicationService serviceImplementation="com.testbuddy.settings.SettingsService"/>
<extensions>
```

## The PSI
The Program Structure Interface (PSI) is IntelliJ's internal represantation of the code. It is biderectional meaning that a change in the code is refected in the PSI and a change in the PSI is reflected in the code (the text buffer).

### Java Specific PSI
If you want to use the PSI to examine and alter Java code you will have to import a dependency. Assuming you are using gradle you can do this by including the following line in `gradle.properties` file 
```
platformPlugins = com.intellij.java
```

### Traversing the PSI
Rougly speaking the PSI is a tree (or more accurately a graph) representation of the code. Therefore the main principle behind traversing it is that you go from parent elements to children and from the children to the parents. 

One of the common use cases for traversing the PSI is finding a specific type of child from the parent. For example let's say you want to find all the methods in this class. Thankfully there exists a useful utility class that alows you to perform various queries and operations on the PSI; this class is called `PsiTreeUtil`. To get back on our example, here is the code to find all the methods in a file:

```kotlin=
fun findMethods(file: PsiFile) {
    val methods : MutableCollection<PsiMethod!> = PsiTreeUtil.findChildrenOfType(file, PsiMethod::class.java)
}
```

Similarly you could use `PsiTreeUtil.findParentOfType` to find parents of a specific file. Also note that you have access to the PsiFile via the `AnActionEvent` `getData` method.


### Types of Psi Elements 
Most classes and interfaces related to the PSI have PsiElement as an ancestor. Here we list the most important interfaces and classes that we have found useful during development. Note that this is by no means an exhaustive list.

* `PsiIfStatement`
* `PsiSwitchStatement`
* `PsiParameter`
* `PsiMethod`
* `PsiWhileStatement`
* `PsiForStatement`
* `PsiDoWhilStatement`
* `PsiClass`


### Tips and Tricks
* Use the PsiViewer IDEA plugin to inspect the PSI of the project you are currently editing.
* Documentation about the PSI is scarse, use Codota to find snippets of code about classes you are interested in and your IDE's suggestions to inspect the available methods on each object.

# Snippets

## Moving the caret

```kotlin
// you can get the 'primaryCaret' from the editor.caretModel
// the caretModel also has information about secondary carets
val caret = editor.caretModel.primaryCaret

// you can move the caret to an offset
// if you want to go to a psi element you can do psiElement.startOffset 
caret.moveToOffset(offset)
```

For more information look at the [Editors wiki page]( https://plugins.jetbrains.com/docs/intellij/editors.html)

## Scrolling the editor

```kotlin
// the scrollingModel allows you to do all scroll operations
editor.scrollingModel.scrollToCaret(ScrollType.CENTER)
```

## Creating basic templates

```kotlin
val templateManager = TemplateManager.getInstance(project)

// you can pass empty strings if you dont want to assign a key
// or a group
val template = templateManager.createTemplate("key", "group")

// you can add text segments
template.addTextSegment("template: ")

// as well as variables
template.addVariable("VAR", ConstantNode("defaultValue"))

// do this if you want the template to be formatted according to the code style when called
template.isToReformat = true

// you can start the template from the code
templateManager.startTemplate(editor, template)
```

## Highlighting code elements

```kotlin
val textAttributes = TextAttributes()
textAttributes.backgroundColor = Color.CYAN

editor.markupModel.addRangeHighlighter(
    startOffset, // (int) offset of the start of the highlight
    endOffset, // (int) offset of the end of the highlight
    layer, // (int) layer to place the highlight on (usually zero)
    textAttributes,
    HighlighterTargetArea.EXACT_RANGE)
val