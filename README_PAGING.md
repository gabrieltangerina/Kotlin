# Pading3
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

O código a seguir mostra uma função que faz uma requisição para a API. Nessa função está presente a url, os parâmetros que serão enviados e o tipo de retorno. Perceba que o retorno é do tipo da data class mostrada anteriormente ```BasePaginationRemote```. Além disso, essa data class recebeu uma lista de objetos ```MovieResponse``` que é uma data class que recebe os retornos da API para um filme:
```kotlin
  @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("with_genres") genreId: Int?,
        @Query("page") page: Int?
    ): BasePaginationRemote<List<MovieResponse>>
```

<br>

# Passos para implementar a paginação:

## 1. Criação do PagingSource
É uma classe responsável por fornecer os dados paginados. Ele lida com a lógica de recuperação de dados de uma fonte de dados (como uma API ou banco de dados) e a paginação desses dados.

<br>

Essa classe 2 métodos: <br>
load(): Método principal onde você define como carregar uma página de dados. <br>
getRefreshKey(): Método para determinar qual chave (página) deve ser usada ao recarregar os dados. <br>

<br>

A própria documentação do Paging3 já nos traz uma PagingSource padrão. Vou passar a seguir um código do projeto MovieApp que está nesse repositório para explicar as mudanças essenciais da classe PagingSource. Vale ressaltar que o código anterior, o ```getMoviesByGenre()``` retorna uma ```BasePaginationRemote``` e é dessa requisição que vamos tratar a paginação:
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

<br>

Outra alteração foi em:
```kt
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
```

<br>

```kt
  val page = params.key ?: DEFAULT_PAGE_INDEX
```

<br>

Este trecho de código determina a chave da página atual que deve ser carregada. O valor de ```params.key``` contém a chave da página que está sendo solicitada. Se esta é a primeira vez que o código está carregando dados (ou seja, params.key é null), ele utilizará DEFAULT_PAGE_INDEX como o valor da página inicial:
```DEFAULT_PAGE_INDEX``` está em um arquivo chamado ```Constants``` criado dentro da pasta ```util```. Nessa arquivo ficam armazenados as variáveis contantes usadas em diferentes partes do app:
```kt
  class Constants {
  
      object Paging {
          const val NETWORK_PAGE_SIZE = 20
          const val DEFAULT_PAGE_INDEX = 1
      }
  
  }
```
O valor dessas contantes podem variar de acordo com o retorno da API que você está utlizando

<br>

```kt
  val result = serviceAPI.getMoviesByGenre(
      genreId = genreId,
      page = page
  ).results ?: emptyList()
```

A variável ```result``` armazena a lista de ```MovieResponse``` retornada da requisição ```getMoviesByGenre()```. Mais precisamente, a requisição retornou um ```BasePaginationRemote<List<MovieResponse>>``` mas usando o ```.results``` você consegue acessar a lista de ```MovieResponse```.


<br>

Dessa forma, com os valores de ```page``` e ```result``` já é possível carregar a paginação:
```kt
  return LoadResult.Page(
    data = result,
    prevKey = if(page == DEFAULT_PAGE_INDEX) null else page - 1,
    nextKey = if(result.isEmpty()) null else page + 1
  )
```

```prevKey```: Indica a chave da página anterior. É usada quando o usuário faz scroll para cima, querendo ver dados anteriores. <br>
```nextKey```: Indica a chave da próxima página. É usada quando o usuário faz scroll para baixo, querendo ver mais dados.


<br>

## 2. Como fica o Repository e RepositoryImpl

A interface repository precisa ter o mesmo tipo de dado retornado do ```PagingSource```, então ela deve ficar assim:
```kt
interface MovieRepository {

  fun getMoviesByGenre(
    genreId: Int?
  ): PagingSource<Int, MovieResponse>

}
```

<br>

Assim, a classe que implementa essa interface terá que fazer a alteração também:
```kt
  class MovieRepositoryImpl @Inject constructor(
    private val serviceAPI: ServiceAPI
) : MovieRepository {

    override fun getMoviesByGenre(
        genreId: Int?
    ): PagingSource<Int, MovieResponse> {
        return MovieByGenrePagingSource(serviceAPI, genreId)
    }

}
```

A alteração também é feita no corpo da função. Ao inves de retornar o método do serviceAPI, ela irá chamar o ```MovieByGenrePagingSource()```

<br>

## 3. Como fica o UseCase

<br>

```kt
class GetMoviesByGenreUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(
        genreId: Int?
    ): Flow<PagingData<Movie>>  = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = DEFAULT_PAGE_INDEX
        ),
        pagingSourceFactory = {
            movieRepository.getMoviesByGenre(genreId)
        }
    ).flow.map { pagingData ->
        pagingData.map { movieResponse ->
            movieResponse.toDomain()
        }
    }

}
```

<br>

O retorno do UseCase passará a ser um ```Flow<PagingData<Movie>>``` e usará o ```Pager``` para criar e gerenciar a instância do PagingSource e controlar a lógica da paginação. 

<br>

```PagingConfig``` configura como a paginação deve funcionar, passando a quantidade de itens por página, se os itens serão exibidos caso seus dados ainda não estiverem disponíveis e o tamanho da carga inicial de dados. <br>
```pagingSourceFactory``` é uma função lambda que retorna uma instância de PagingSource. Nesse exemplo o getMoviesByGenre() retorna um PagingSource. <br>
    
<br>

O ```Pager()``` retorna um ```Flow``` que são os dados paginados. Esses dados são coletados, transformados para a camada Domain e depois podem ser exibidor na UI depois de passar pela ViewModel.

<br>

## 4. Como fica o ViewModel

<br>

```kt
class MovieGenreViewModel @Inject constructor(
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase
) : ViewModel() {

    private val _movieList = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val movieList get() = _movieList.asStateFlow()

    private var currentGenreId: Int? = null

    fun getMoviesByGenre(genreId: Int?, forceRequest: Boolean) = viewModelScope.launch {
        if (genreId != currentGenreId || forceRequest) {
            currentGenreId = genreId
            getMoviesByGenreUseCase(
                genreId = genreId
            ).cachedIn(viewModelScope).collectLatest { pagingData ->
                _movieList.emit(pagingData)
            }
        }
    }

}
```

<br>

A verificação ```genreId != currentGenreId``` é para manter a listagem quando você troca de telas. A requisição para a listagem só será chamada quando o id do gênero for diferente da anterior. Dessa forma, se você estiver navegando pela listagem, clicar em um filme por exemplo, irá abrir a tela de detalhes e se você voltar para a tela anterior a listagem estará onde você parou.

<br>

## 4. Como fica o fragment

<br>

```kt
private fun getMoviesByGenre(forceRequest: Boolean = false) {
    lifecycleScope.launch {
        viewModel.getMoviesByGenre(genreId = args.genreId, forceRequest = forceRequest)
        viewModel.movieList.collectLatest { pagingData ->
            moviePagingAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }
    }
}
```
