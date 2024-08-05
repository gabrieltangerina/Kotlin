# Estudos Sobre Kotlin

## Descrição
Este repositório contém minhas anotações e exemplos de código enquanto estudo Kotlin. Aqui você encontrará soluções para problemas comuns, dicas e truques, e implementações práticas de funcionalidades específicas.

## Conteúdos
- [Paging3 - Scroll Infinito](https://github.com/gabrieltangerina/Kotlin/blob/e75915671b522814213663c3f9317dd0606edf07/README_PAGING.md)

## Anotações 

### Funcinalidade de teclado e searchView. 
No primeiro clique do botão de voltar do dispositivo durante uma pesquisa, o teclado é minimizado, porém o searchView continua ativo. No segundo clique, o usuário será retornado para a tela anterior. O código a seguir fará com que no segundo clique
do botão de voltar o foco no searchView é desabilitado e ele será fechado, e apenas no terceiro clique do usuário ao botão de voltar do dispositivo, será redirecionado para a tela anterior

```kotlin 
  private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.simpleSearchView.isVisible) {
                        binding.simpleSearchView.closeSearch()
                    } else {
                        findNavController().popBackStack()
                    }
                }
            })
    }
```

<br>

### Adapter

Um adapter é uma classe que trabalha como intermediador entre um AdapterView, que pode ser um RecyclerView, ListView entre outros, e os dados que precisam ser exibidos nesses componentes. <br>

Ele é responsável por pegar os dados do backend e convertê-los em itens de visualização que serão exibidos em um RecyclerView por exemplo.

#### Principais Métodos do Adapter

#### 1. onCreateViewHolder()

Esse método é chamado quando o RecyclerView precisa de um novo ViewHolder na listagem. Um ViewHolder é cada item no RecyclerView, ele representa o layout do item. Esse método infla o layout do item e cria uma instância do ViewHolder. <br>

```kt
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
    return MyViewHolder(
        ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )
}
```

#### 2. onBindViewHolder()

Esse método é responsável por adicionar o conteúdo em cada ViewHolder, exibindo os dados corretos para a posição correta.

```kt
override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comment = getItem(position)

        holder.binding.textUsername.text = comment.authorDetails?.username
        holder.binding.textComment.text = comment.content
        holder.binding.textRating.text = comment?.authorDetails?.rating?.toString() ?: "0"
        holder.binding.textDate.text = formatCommentDate(comment.createdAt)

}
```

#### 3. getItemCount()

Esse método retorna o número total de itens usados na listagem. <br>

```kt
 override fun getItemCount(): Int {
  return items.size
}
```

#### 4. MyViewHolder()

Não é criada automaticamente pela adapter. Sua função é manter referência aos componentes de dentro de cada item da lista. <br>

```kt
inner class MyViewHolder(val binding: MovieGenreItemBinding)
      : RecyclerView.ViewHolder(binding.root)
```
