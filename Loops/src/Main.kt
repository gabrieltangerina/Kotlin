
fun main(){
    val pessoas = listOf("Gabriel", "Tangerina", "Gonzalez")

    for ((posicao, pessoa) in pessoas.withIndex()){
        println("$pessoa está na $posicao ")
    }

    for(pessoa in pessoas){
        println(pessoa)
    }

    pessoas.forEach{ println(it) }
    pessoas.forEach{ pessoa ->
        println(pessoa)
    }

    pessoas.forEachIndexed{ index, pessoa ->
        println("Posicao $index Nome $pessoa")
    }

    val filtro = pessoas.filter { it.startsWith('g', true) }
    println(filtro)

}