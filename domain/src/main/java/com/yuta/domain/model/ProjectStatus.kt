package com.yuta.domain.model

enum class ProjectStatus(private val text: String) {

    PROCESS("в работе"),
    PAUSE("приостановлен"),
    COMPLETE("завершен");

    override fun toString(): String = text

    companion object {
        fun fromText(text: String): ProjectStatus {
            return entries.find { it.text == text }
                ?: throw IllegalArgumentException("Unknown value: $text")
        }
    }
}
