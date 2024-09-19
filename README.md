# Estudos Sobre Kotlin

## Descrição
Este repositório contém minhas anotações e exemplos de código enquanto estudo Kotlin. Aqui você encontrará soluções para problemas comuns, dicas e truques, e implementações práticas de funcionalidades específicas.

## Conteúdos
- [Paging3 - Scroll Infinito](https://github.com/gabrieltangerina/Kotlin/blob/e75915671b522814213663c3f9317dd0606edf07/README_PAGING.md)
- [Adapter - Listagem de conteúdo](https://github.com/gabrieltangerina/Kotlin/blob/e75915671b522814213663c3f9317dd0606edf07/README_PAGING.md)
- [Permissão - Uso de câmera e galeria do dispositivo](https://github.com/gabrieltangerina/Kotlin/blob/e75915671b522814213663c3f9317dd0606edf07/README_PERMISSION.md)

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

### Navegação Global: Encerrando a Fila de Telas ao Acessar Outra Tela
Essa funcionalidade é muito útil em situações como, por exemplo, após um login, quando você não deseja que o usuário consiga voltar para a tela anterior. Isso pode ser alcançado utilizando navegações comuns ou navegações globais.

As navegações globais auxiliam a reduzir a complexidade do gráfico de navegação, tornando-o mais organizado. Ao usar navegação global, é possível limpar a pilha de telas, garantindo que o usuário não possa retornar às telas anteriores. Para isso, o seguinte código pode ser utilizado:
```kotlin
  import androidx.navigation.NavOptions

  val navOptions: NavOptions =      // Tela que você deseja fechar
      NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()

                               // Navegação que deseja ir
  findNavController().navigate(R.id.action_global_homeFragment, null, navOptions)
```
