

fun main(){

    val user1 = Usuario("Gabriel", 1.82f)

    val user2 = Usuario("Fulano",1.75f)

    println(user1.info())
    println(user2.info())
}

class Usuario(nome: String, altura: Float){
    var nome: String = nome
    var altura: Float = altura

    fun info():String {
        return "Usuário: $nome altura: $altura"
    }
}
