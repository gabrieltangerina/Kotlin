Em andamento...

# Permissão Câmera e Galeria
Para a permissão de galeria, você precisará criar um código para os celulares Android com versões ≤ 12 e outra para Android ≥ 13.
<br>

## BottomSheets para as permissões
Foi usado BottomSheets tanto para informar se irá acessar a galeria ou a câmera quanto para informar a necessidade da permissão se o usuário nega-la.

### BottomSheet para escolher entra câmera e galeria
Foi criado uma BottomSheet com as opções de câmera e galeria e ao usuário clicar em uma das opções é chamado os métodos necessários.

```kotlin
private fun openBottomSheetSelectImage() {
  val bottomSheetDialog =BottomSheetDialog(requireContext(),R.style.BottomSheetDialog)

  val bottomSheetBinding = BottomSheetSelectImageBinding.inflate(
      layoutInflater, null, false
  )

  bottomSheetBinding.btnCamera.setOnClickListener {
      bottomSheetDialog.dismiss()
      checkCameraPermission()
  }

  bottomSheetBinding.btnGallery.setOnClickListener {
      bottomSheetDialog.dismiss()
      checkGalleryPermission()
  }

  bottomSheetBinding.btnClose.setOnClickListener { bottomSheetDialog.dismiss() }

  bottomSheetDialog.setContentView(bottomSheetBinding.root)
  bottomSheetDialog.show()
}
```

<img height="500px" src="https://github.com/gabrieltangerina/Kotlin/blob/main/images_readme/permission_images/BottomSheet.png?raw=true"/>

### BottomSheet para pedir permissão ao usuário
Existe outra BottomSheet que é a openBottomSheetPermissionDenied() que é chamada quando o usuário não aceita a permissão então ela é mostrada informando o usuário que é necessário aceitar a permissão para acessar aquela tela.

```kotlin
private fun openBottomSheetPermissionDenied() {
    val bottomSheetDialog =BottomSheetDialog(requireContext(),R.style.BottomSheetDialog)

    val bottomSheetBinding = BottomSheetPermissionDeniedBinding.inflate(
        layoutInflater, null, false
    )

    bottomSheetBinding.btnCancel.setOnClickListener {
        bottomSheetDialog.dismiss()
    }

    bottomSheetBinding.btnAccept.setOnClickListener {
        bottomSheetDialog.dismiss()

        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireActivity().packageName, null)
        )

        startActivity(intent)
    }

    bottomSheetDialog.setContentView(bottomSheetBinding.root)
    bottomSheetDialog.show()
}
```

<img height="500px" src="https://github.com/gabrieltangerina/Kotlin/blob/main/images_readme/permission_images/BottomSheetPermissionDenied.png?raw=true"/>

## Carregar imagem da galeria versão Android <= 12

1. Adicionar a permissão no <strong>AndroidManifest</strong>
```kotlin
// Para Android 12- Gallery
<uses-permission
  android:name="android.permission.READ_EXTERNAL_STORAGE"
  android:maxSdkVersion="32" />
```

2. Crie uma função na sua tela que verifica se a permissão de acessar a galeria foi aceita
```kotlin
private val GALLERY_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE

private fun checkGalleryPermission() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        // Android 12-
        if (checkPermissionGranted(GALLERY_PERMISSION)) {
            pickImageLauncher.launch("image/*")
        } else {
            galleryPermissionLauncher.launch(GALLERY_PERMISSION)
        }
    } else {
        // Android 13+
        pickMedia.launch(PickVisualMediaRequest(
	        ActivityResultContracts.PickVisualMedia.ImageOnly)
	      )
    }
}
``` 

3. Crie uma função que recebe uma permissão e retorna se ela foi aceita ou não. Ela é usada tanto para a permissão de galeria quanto para a permissão de câmera.
```kotlin
private fun checkPermissionGranted(permission: String) =
ContextCompat.checkSelfPermission(
    requireContext(),
    permission
) == PackageManager.PERMISSION_GRANTED
```

4. Solicita a permissão para o usuário em tempo de execução
```kotlin
private val galleryPermissionLauncher =
  registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
      if (isGranted) {
          pickImageLauncher.launch("image/*")
      } else {
          openBottomSheetPermissionDenied()
      }
  }
```

5. Obtenha a imagem selecionada na galeria e envia para o ImageView da UI
```kotlin
// Used in Android <= 12
private val pickImageLauncher = registerForActivityResult(
    ActivityResultContracts.GetContent()
) { uri: Uri? ->
    uri?.let {
        binding.imageProfile.setImageURI(it)
    }
}
```

### Como funciona o algoritmo:
Quando o usuário tocar no botão de escolher foto no BottomSheet é chamado o checkGalleryPermission(), esse método solicita a permissão se ainda não a possui ou já abre a galeria chamando o pickImageLauncher que retorna uma URI q é o caminho da imagem na galeria do dispositivo e a adiciona no imageView.

## Carregar imagem da galeria versão Android >= 13

1. Adicionar a permissão no AndroidManifest
```kotlin
<uses-permission
	android:name="android.permission.READ_MEDIA_IMAGES" />
```

2. No checkGalleryPermission já existe o tratamento usado para o Android 13+ então basta mantê-lo igual.O que difere é o pickMedia, ele é usado para obter a imagem:

```text
O pickMedia é usado para obter a imagem do Android 13+
O pickImageLauncher é usado para obter a imagem do Android 12-
```

```kotlin
// Used in Android >= 13
private val pickMedia =
    registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            binding.imageProfile.setImageURI(it)
        }
    }
```




