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
A biblioteca Paging do Android Jetpack facilita a implementação do carregamento paginado de dados em aplicativos Android. A paginação é essencial quando se lida com grandes volumes de dados, como feeds infinitos em aplicativos de redes sociais. Esses dados podem vir de um banco de dados local, uma API remota, ou qualquer outra fonte de dados.

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
      ...
    {
      "id": 10,
      "name": "Item 10"
    }
  ]
}
```

CONTINUA...
