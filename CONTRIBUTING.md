# Contributing to TestBuddy
The following is a set of guidelines for contributing to TestBuddy. 
Please read them carefully before starting to write code.

## Git Workflow
We use the [Git-flow](https://nvie.com/posts/a-successful-git-branching-model/) branching model. In short, we have the *master* branch where stable builds of 
the project can be found. Parallel to the master branch we have the *develop* branch where we keep the latest changes in development. 
You should always branch out from develop to implement any new features. 

## Opening issues
To create a new issue please use the corresponding Gitlab features. 
If the issue regards a request for a new feature please use the [feature template](.gitlab/issue_templates/feature_template.md). 
If the issue is about a bug report please use the [bug template](.gitlab/issue_templates/bug_template.md). 
Before submitting the issue make sure to mark it with the matching labels.

## Style guides
To keep a unified style in the code base and in the repository we ask you abide by the following style guides.

### Code Style & Static Analysis
We use [ktlint](https://ktlint.github.io/) to enforce code styling rules based on the [official kotlin code style](https://kotlinlang.org/docs/coding-conventions.html).
You do not have to worry about the specific rules **as long as you run ktlintFormat before pushing**. 

We use [detekt](https://detekt.github.io/detekt/) for static analysis of the code. It is set up to use the [default configuration](https://github.com/detekt/detekt/blob/main/detekt-core/src/main/resources/default-detekt-config.yml). 

### KDoc
All classes and methods/functions should have corresponding KDoc to briefly explain their purpose.
The KDoc should define the parameters and return values. Here is an example taken from the [Kotlin documentation](https://kotlinlang.org/docs/kotlin-doc.html#block-tags):
```kotlin
/**
 * A group of *members*.
 *
 * This class has no useful logic; it's just a documentation example.
 *
 * @param T the type of a member in this group.
 * @property name the name of this group.
 * @constructor Creates an empty group.
 */
class Group<T>(val name: String) {
    /**
     * Adds a [member] to this group.
     * @return the new size of the group.
     */
    fun add(member: T): Int { ... }
}
``` 

### Git Commit Messages
All commit messages should start with a capital letter and use present tense, with no punctuation.
Here is an example: `Add Contributing.md` 

## Pushing and Merging
Before pushing your work to the remote please make sure to:
* Pull and resolve any merge conflicts locally
* Run `ktlintFormat` to fix linting issues with your code
* Make sure the project builds and all style checks and tests pass

Now you can push to the remote. After that feel free to open a merge request for your feature.
Use the [merge request template](.gitlab/merge_request_templates/default.md) to explain your changes.
Once you have submitted the request, keep an eye out for any comments that might come up during the review process. 
Once the review is done, you can merge your work.