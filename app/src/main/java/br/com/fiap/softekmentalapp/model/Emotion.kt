package br.com.fiap.softekmentalapp.model

sealed class Emotion(val id: Int, val routeParam: String) {
    object Happy : Emotion(1, "feliz")
    object Sad : Emotion(2, "triste")
    object Anxious : Emotion(3, "ansioso")
}