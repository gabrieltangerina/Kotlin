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
