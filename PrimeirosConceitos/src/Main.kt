
const val idade:Int = 18

fun main() {
    val nome:String = "Gabriel"
    var sobrenome:String = "Gonzalez"

    sobrenome = "Tangerina"

    println("$nome $sobrenome possui $idade")

    listas()
}


fun listas(){
    val nomes = listOf("Gabriel", "Tangerina", "Gonzalez")

    println(nomes)

    val filmes = mutableListOf("Django")
    filmes.add("Como treinar o seu dragão")
    filmes.add("Creed I")
    filmes.removeAt(1)

    println(filmes)
    println("Gabriel" in nomes)
}