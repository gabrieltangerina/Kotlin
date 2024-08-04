# Estudos Sobre Kotlin

## Descrição
Este repositório contém minhas anotações e exemplos de código enquanto estudo Kotlin. Aqui você encontrará soluções para problemas comuns, dicas e truques, e implementações práticas de funcionalidades específicas.

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

<br><br><br>

### Pading 3
A biblioteca <a href="https://developer.android.com/topic/libraries/architecture/paging/v3-overview?hl=pt-br">Paging 3</a> do Android Jetpack facilita a implementação do carregamento paginado de dados em aplicativos Android. A paginação é essencial quando se lida com grandes volumes de dados, como feeds infinitos em aplicativos de redes sociais. Esses dados podem vir de um banco de dados local, uma API remota, ou qualquer outra fonte de dados.

Aplicativos que utilizam paginação geralmente interagem com APIs paginadas. Isso significa que, em vez de retornar todos os resultados de uma vez, a API devolve um subconjunto de resultados de um conjunto maior para cada requisição. Um exemplo de resposta JSON paginada é o seguinte:

```json
  {
  "page": 1,
  "pageSize": 10,
  "totalPages": 100,
  "totalResults": 1000,
  "results": [
    {
      "id": 1,
      "name": "Item 1"
    },
    {
      "id": 2,
      "name": "Item 2"
    },
      {...}
    {
      "id": 10,
      "name": "Item 10"
    }
  ]
}
```

<br>
Para capturar a estrutura de resposta paginada vinda em JSON de uma requisição, você pode usar uma data class como a seguinte:

<br>
<br>

```kotlin
  data class BasePaginationRemote<out T>(
    @SerializedName("page")
    val page: Int?,
  
    @SerializedName("results")
    val results: T?,
  
    @SerializedName("total_pages")
    val totalPages: Int?,
  
    @SerializedName("total_results")
    val totalResults: Int?
  )
```

<br>

O código a seguir mostra uma função que faz uma requisição para a API. Nessa função está presente a url, os parâmetros que serão enviados e o tipo de retorno. Perceba que o retorno é do mesmo tipo da data class mostrada anteriormente ```BasePaginationRemote```. Além disso, essa data class recebeu uma lista de objetos ```MovieResponse``` que é uma data class que recebe os retornos da API para um filme.

<br>

```kotlin
  @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("with_genres") genreId: Int?,
        @Query("page") page: Int?
    ): BasePaginationRemote<List<MovieResponse>>
```

<br>
A própria documentação do Paging3 já nos traz uma classe padrão para implementa-la. Vou passar a seguir um código do projeto MovieApp que está nesse repositório para explicar as mudanças essenciais para que a paginação funcione. Vale ressaltar que o código anterior, o 

```getMoviesByGenre()``` retorna uma ```BasePaginationRemote``` e é ela que está sendo tratada no código a abaixo.

<br>
 
```kotlin
  class MovieByGenrePagingSource(
    private val serviceAPI: ServiceAPI,
    private val genreId: Int?
) : PagingSource<Int, MovieResponse>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MovieResponse> {
        return try {
            val page = params.key ?: DEFAULT_PAGE_INDEX
            val result = serviceAPI.getMoviesByGenre(
                genreId = genreId,
                page = page
            ).results ?: emptyList()
            return LoadResult.Page(
                data = result,
                prevKey = if(page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if(result.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
```

<br>
As principais alterações que foram feitas no código base vindo da documentação da bilioteca foram:

<br>
<br>

```kt
  class MovieByGenrePagingSource(
      private val serviceAPI: ServiceAPI,
      private val genreId: Int?
  ) : PagingSource<Int, MovieResponse>() {...}
```

<br>

<strong>Os parâmetros</strong>, agora ela recebe a classe ```ServiceAPI``` que é responsável por armazenar os dados necessários para as requisições, como url, parâmetros e retorno. Além disso, ela recebe o ```genreId``` que é usado na url da requisição. 

<br>

<strong>O tipo de retorno</strong>, perceba que o retorno ```<Int, MovieResponse>``` aparece algumas vezes no código. O ```Int``` faz relação ao tipo de dado da paginação, como página 1, 2, 3 e assim por diante. E o segundo é o tipo de dado que será retornado da nossa paginação, se você voltar no código da requisição verá que o retorno é ```BasePaginationRemote<List<MovieResponse>>```, logo o tipo de retorno é ```MovieResponse```.
