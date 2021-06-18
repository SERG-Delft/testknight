package com.testknight.utilities

class StringFormatter private constructor() {

    companion object {
        /**
         * Formats the name of the method to be of the form: "methodName".
         *
         * @param methodName the full name of the method.
         * @return a String representing the formatted method name.
         */
        fun formatMethodName(methodName: String): String {
            return if (methodName.contains(".")) {
                methodName.substring(methodName.lastIndexOf(".") + 1)
            } else {
                methodName
            }
        }

        /**
         * Formats the name of the field that is affected so that
         * all fields have the form "this.nameOfField".
         *
         * @param name the name of the field.
         * @return the formatted version of the name.
         */
        fun formatClassFieldName(name: String): String {
            return if (name.startsWith("this.")) {
                name.replaceFirst("this.", "")
            } else {
                name
            }
        }
    }
}
