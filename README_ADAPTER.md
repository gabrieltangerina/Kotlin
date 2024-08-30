# Adapter Padrão e Adapter com DiffUtil

Um Adapter é uma classe que atua como um intermediário entre um AdapterView, que é um componente de listagem exemplo RecyclerView, ListView, entre outros, e os dados que precisam ser exibidos nesses componentes. <br>

Ele é responsável por pegar os dados do backend ou outra fonte de dados e convertê-los em itens de visualização que serão exibidos, por exemplo, em um RecyclerView. <br>

Para você fazer uma listagem nativamente para Android você irá precisar de 3 arquivos:
- Activity ou Fragment com um componente de listagem: Quando você cria uma Activity ou Fragment, também é gerado automaticamente um layout associado, que define a interface do usuário. Esse layout conterá o componente de listagem, como um RecyclerView, e o arquivo de código correspondente implementará a lógica por trás da interface.
- Adapter: O foco principal aqui é o Adapter, uma classe que serve como intermediária entre o componente de listagem no layout e os dados que precisam ser exibidos. Ele converte os dados do backend em itens de visualização para serem apresentados no RecyclerView ou outros componentes de listagem.
- Layout do item: Este layout define a interface de cada item individual na listagem. Ele especifica como os dados serão apresentados visualmente, com os componentes de UI que representarão um único item na lista, como textos, imagens, e botões.

## Principais Métodos do Adapter

### 1. onCreateViewHolder()

Esse método é chamado quando o RecyclerView precisa de um novo ViewHolder para representar um item na lista. O ViewHolder encapsula o layout de cada item individual, permitindo que os dados sejam exibidos de maneira eficiente. Dentro do onCreateViewHolder(), o layout do item é inflado, ou seja, convertido de um arquivo XML para um objeto View, e uma instância do ViewHolder é criada para gerenciar esse layout.

Vale lembrar que o layout de cada item é definido por nós, geralmente em um arquivo XML separado, onde especificamos a estrutura e o estilo dos elementos que compõem cada item na lista. Um padrão comum é nomear esses arquivos com a palavra ```item_``` seguida de uma descrição do que o layout representa, como ```item_comment.xml``` ou ```item_movie.xml```. Esse layout personalizado é então inflado dentro do onCreateViewHolder() para criar a estrutura visual de cada item antes de ser vinculado aos dados pelo onBindViewHolder().

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

### 2. onBindViewHolder()

Este método é responsável por associar os dados corretos a cada ViewHolder em uma posição específica da lista. Nele, você define como os dados devem ser exibidos em cada item do RecyclerView. O método é chamado para cada item visível na lista, onde os valores dos componentes de UI são atualizados conforme os dados correspondentes à posição atual, como em ```val comment = getItem(position)```.

Além disso, é nesse ponto que você pode realizar verificações e ajustes nos componentes de UI com base nos dados, como alterar a cor de fundo de itens que atendam a determinados critérios específicos.

```kt
override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comment = getItem(position)

        holder.binding.textUsername.text = comment.authorDetails?.username
        holder.binding.textComment.text = comment.content
        holder.binding.textRating.text = comment?.authorDetails?.rating?.toString() ?: "0"
        holder.binding.textDate.text = formatCommentDate(comment.createdAt)

}
```

### 3. getItemCount()

Esse método retorna o número total de itens usados na listagem. Com ele o RecyclerView sabe quantos itens ele precisa exibir, ajudando-o a determinar o tamanho da lista e a quantidade de elementos a serem renderizados. <br>

```kt
 override fun getItemCount(): Int {
  return items.size
}
```

### 4. MyViewHolder()

O ViewHolder não é criado automaticamente pelo Adapter. Sua função é manter a referência aos componentes dentro de cada item da lista, facilitando o acesso e a manipulação desses componentes. <br>

```kt
inner class MyViewHolder(val binding: MovieGenreItemBinding)
      : RecyclerView.ViewHolder(binding.root)
```

## Diferença entre o Adapter padrão e o Adapter com DiffUtil

### Primeiramente, o que seria esse DiffUtil? 

O DiffUtil é uma classe utilitária que ajuda o RecyclerView.Adapter a calcular as diferenças entre uma lista antiga e uma nova, e atualizar apenas os itens que mudaram. Ele é útil para otimizar a performance do RecyclerView, especialmente quando se trabalha com listas grandes.

### Quais são as principais diferentes entre eles?

A principal diferença entre um adapter comum e um adapter que utiliza DiffUtil está na forma como é configurado, já que é necessário configurar o DiffUtil no adapter. Além disso, há uma diferença na forma como a lista de dados é enviada e atualizada. 

Para enviar a lista com os dados que serão listados para o adapter comum você os passa no construtor da classe:
```kt
class MyAdapter(private val itemList: List<MyDataClass>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {...}
```

Para atualizar a lista no adapter comum:
```kt
    adapter.notifyDataSetChanged() // recarrega toda a lista
    adapter.notifyItemChanged(int position) // recarrega um único item
    adapter.notifyItemInserted(int position) // adicionar item específico
    adapter.notifyItemRemoved(int position) // remover item específico
```

Agora, no adapter com o DiffUtil tanto para enviar a lista quando para atualiza-la você utiliza o ```submitList()```:
```kt
    adapterItens.submitList(itensList)
```

### Como fica um adapter configurado com DiffUtil:
```kt
class TransactionsAdapter(
    private val context: Context,
    private val transactionSelected: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionsAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(
                oldItem: Transaction,
                newItem: Transaction
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Transaction,
                newItem: Transaction
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TransactionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ...
    }

    inner class ViewHolder(val binding: TransactionItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}
```

