@file:OptIn(ExperimentalLayoutApi::class)

package com.example.ip_test_task

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ip_test_task.database.DTO.ProductDTO
import com.example.ip_test_task.database.ProductDatabase
import com.example.ip_test_task.ui.theme.IptesttaskTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val fakeViewModelInstance = FakeViewModelClass(this)

        CoroutineScope(Dispatchers.IO).launch {
            val database = ProductDatabase.getDbInstance(this@MainActivity)
            val products = database.productDao.selectAllProducts()

            Log.i("Products count", products.size.toString())

            if (products.isEmpty()) {
                withContext(Dispatchers.Main) {
                    fakeViewModelInstance.loadProducts(10)
                }
            }
        }

        setContent {
            IptesttaskTheme {
                Surface(
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.background
                    )
                ) {
                    Column {
                        TopBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.2f)
                                .align(Alignment.CenterHorizontally)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        Spacer(
                            modifier = Modifier
                                .height(16.dp)
                        )
                        SearchBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                                .background(MaterialTheme.colorScheme.surface),
                            hint = "Введите имя товара / тег"
                        ) {
                            fakeViewModelInstance.searchProduct(it)
                        }
                        Spacer(
                            modifier = Modifier
                                .height(15.dp)
                        )
                        ProductList(
                            fakeViewModel = fakeViewModelInstance,
                            context = this@MainActivity
                        )
                    }
                }
            }
        }
    }
}

//suspend fun fillDatabase(context: Context) {
//    //МНЕ НЕ ПОНРАВИЛАСЬ СТРУКТУРА (ТО ЧТО ТЕГИ ИДУТ ПРОСТО СТРОКОЙ) В ПРЕДОСТАВЛЕНННОЙ БАЗЕ ДАННЫХ ПОЭТОМУ ПРИШЛОСЬ ВРУЧНУЮ ВСЕ ВПИЛИВАТЬ
//    var productInitialList = listOf(
//        ProductDTO(
//            id = null,
//            name = "iPhone 13",
//            amount = 15,
//            time = LocalDateTime.ofInstant(
//                Instant.ofEpochMilli(1633046400000), ZoneId.of("Europe/Moscow")
//            ),
//            tags = listOf("Телефон", "Новый", "Хит")
//        ),
//        ProductDTO(
//            id = null,
//            name = "Samsung Galaxy S21",
//            amount = 30,
//            time = LocalDateTime.ofInstant(
//                Instant.ofEpochMilli(1633132800000), ZoneId.of("Europe/Moscow")
//            ),
//            tags = listOf("Телефон", "Хит")
//        ),
//        ProductDTO(
//            id = null,
//            name = "Playstation 5",
//            amount = 7,
//            time = LocalDateTime.ofInstant(
//                Instant.ofEpochMilli(1633219200000), ZoneId.of("Europe/Moscow")
//            ),
//            tags = listOf("Игровая приставка", "Акция", "Распродажа")
//        ),
//        ProductDTO(
//            id = null,
//            name = "LG OLED TV",
//            amount = 22,
//            time = LocalDateTime.ofInstant(
//                Instant.ofEpochMilli(163305600000), ZoneId.of("Europe/Moscow")
//            ),
//            tags = listOf("Телевизор", "Эксклюзив", "Ограниченный")
//        ),
//        ProductDTO(
//            id = null,
//            name = "Apple Watch Series 7",
//            amount = 0,
//            time = LocalDateTime.ofInstant(
//                Instant.ofEpochMilli(163392000000), ZoneId.of("Europe/Moscow")
//            ),
//            tags = listOf("Часы", "Новый", "Рекомендуем")
//        ),
//        ProductDTO(
//            id = null,
//            name = "Xiaomi Mi 11",
//            amount = 5,
//            time = LocalDateTime.ofInstant(
//                Instant.ofEpochMilli(1633478400000), ZoneId.of("Europe/Moscow")
//            ),
//            tags = listOf("Телефон", "Скидка", "Распродажа")
//        ),
//        ProductDTO(
//            id = null,
//            name = "MacBook Air M1",
//            amount = 12,
//            time = LocalDateTime.ofInstant(
//                Instant.ofEpochMilli(1633564800000), ZoneId.of("Europe/Moscow")
//            ),
//            tags = listOf("Ноутбук", "Тренд")
//        ),
//        ProductDTO(
//            id = null,
//            name = "Amazon Kindle Paperwhite",
//            amount = 18,
//            time = LocalDateTime.ofInstant(
//                Instant.ofEpochMilli(1633651200000), ZoneId.of("Europe/Moscow")
//            ),
//            tags = listOf("Электронная книга", "Последний шанс", "Ограниченный")
//        ),
//        ProductDTO(
//            id = null,
//            name = "Fitbit Charge 5",
//            amount = 27,
//            time = LocalDateTime.ofInstant(
//                Instant.ofEpochMilli(1633737600000), ZoneId.of("Europe/Moscow")
//            ),
//            tags = listOf()
//        ),
//        ProductDTO(
//            id = null,
//            name = "GoPro Hero 10",
//            amount = 25,
//            time = LocalDateTime.ofInstant(
//                Instant.ofEpochMilli(1633724000000), ZoneId.of("Europe/Moscow")
//            ),
//            tags = listOf("Камера", "Эксклюзив")
//        )
//    )
//
//    coroutineScope {
//        for (item in productInitialList)
//            ProductDatabase.getDbInstance(context).productDao.upsertProduct(item)
//    }
//}

class FakeViewModelClass(private val context: Context) : ViewModel() {
    private var currentOffset = 0
    var productList = mutableStateOf<List<ProductDTO>>(listOf())
    var endReached = mutableStateOf(false)
    var isLoading = mutableStateOf(true)

    private var isSearchingStarting = true
    private var cachedProductList = listOf<ProductDTO>()
    var isSearching = mutableStateOf(false)

    var inputDialogState = mutableStateOf(false)
    var deleteDialogState = mutableStateOf(false)

    init {
        viewModelScope.launch {
            loadProducts(10)
        }
    }

    fun loadProducts(limit: Int) {
        viewModelScope.launch {
            isLoading.value = true
            val result = ProductDatabase.getDbInstance(context).productDao.selectPaginatedProducts(
                limit
            )
            Log.i("Database", result.size.toString())
            currentOffset += 10
            isLoading.value = false
            productList.value += result
        }
    }

    fun searchProduct(query: String) {
        val listToSearch = if (isSearchingStarting) {
            productList.value
        } else {
            cachedProductList
        }

        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                productList.value = cachedProductList
                isSearching.value = false
                isSearchingStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.name.contains(query.trim(), ignoreCase = true) ||
                        it.tags.any { element ->
                            element.contains(query.trim())
                        }
            }
            if (isSearchingStarting) {
                cachedProductList = productList.value
                isSearchingStarting = false
            }
            isSearching.value = true
            productList.value = results
        }
    }

    fun updateProduct(updatedProduct: ProductDTO) {
        if (!isSearching.value) {
            productList.value = productList.value.map { product ->
                if (product.id == updatedProduct.id) {
                    updatedProduct
                } else {
                    product
                }
            }
            Toast.makeText(context, "все изменилось сохранилось", Toast.LENGTH_LONG).show()
        } else
            Toast.makeText(
                context,
                "ну с поиском изменение не сделал и так много всего напихал",
                Toast.LENGTH_LONG
            ).show()
    }

    fun deleteProduct(deletedProduct: ProductDTO) {
        if (!isSearching.value) {
            productList.value = productList.value.filter { product ->
                product.id != deletedProduct.id
            }
            Toast.makeText(context, "все удалилось сохранилось", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(
                context,
                "ну с поиском удаление не сделал и так много всего напихал",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}


@Composable
fun TopBar(
    modifier: Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = "Список товаров",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
    (LocalView.current.context as Activity).window.statusBarColor =
        MaterialTheme.colorScheme.primary.toArgb()
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf(
            ""
        )
    }

    var isHintDisplayed by remember {
        mutableStateOf(
            hint != ""
        )
    }

    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    5.dp,
                    CircleShape
                )
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    CircleShape
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                )
                .onFocusChanged {
                    isHintDisplayed = !it.isFocused && text.isEmpty()
                }
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                )
            )
        }
    }
}

@Composable
fun ProductList(
    fakeViewModel: FakeViewModelClass,
    context: Context
) {
    val productList by remember {
        fakeViewModel.productList
    }
    val endReached by remember {
        fakeViewModel.endReached
    }
    val isLoading by remember {
        fakeViewModel.isLoading
    }
    val isSearching by remember {
        fakeViewModel.isSearching
    }

    LazyColumn(
        contentPadding = PaddingValues(10.dp)
    ) {
        items(productList.size) {
            //Функционал для автоматической прогрузки списка в будущем
//            if (!endReached
//                && !isLoading
//                && !isSearching
//            ) {
//                LaunchedEffect(
//                    key1 = true
//                ) {
//                    fakeViewModel.loadProducts(10)
//                }
//            }
            ProductEntry(
                productEntry = productList[it],
                context,
                fakeViewModel
            )
            Spacer(
                modifier = Modifier
                    .height(15.dp)
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading)
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun ProductEntry(
    productEntry: ProductDTO,
    context: Context,
    fakeViewModel: FakeViewModelClass
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    var deleteDialog by remember {
        mutableStateOf(false)
    }
    var product by remember {
        mutableStateOf(productEntry)
    }
    val coroutineScope = rememberCoroutineScope()
    if (showDialog) {
        EditDialog(
            iconModifier = Modifier
                .size(48.dp),
            product = product,
            onDismiss = {
                showDialog = false
            },
            onConfirm = { updatedProduct ->
                coroutineScope.launch {
                    ProductDatabase
                        .getDbInstance(context)
                        .productDao
                        .upsertProduct(
                            updatedProduct
                        )
                    fakeViewModel.updateProduct(updatedProduct)
                }

            }
        )
    }
    if (deleteDialog) {
        DeleteDialog(
            iconModifier = Modifier
                .size(48.dp),
            product = product,
            onDismiss = {
                deleteDialog = false
            },
            onConfirm = { deleteProduct ->
                coroutineScope.launch {
                    ProductDatabase
                        .getDbInstance(context)
                        .productDao
                        .deleteProduct(
                            deleteProduct
                        )
                    fakeViewModel.deleteProduct(deleteProduct)
                }

            }
        )
    }
    Box(
        modifier = Modifier
            .shadow(
                5.dp,
                RoundedCornerShape(10.dp)
            )
            .clip(
                RoundedCornerShape(
                    10.dp
                )
            )
            .background(
                MaterialTheme.colorScheme.surface
            )
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column {

            Row(verticalAlignment = Alignment.Top) {

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .padding(start = 15.dp, top = 20.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = productEntry.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                    ) {
                        val pencilModifier = Modifier
                            .padding(10.dp)
                            .size(24.dp)
                            .clickable {
                                showDialog = true
                            }

                        if (isSystemInDarkTheme())
                            Image(
                                painter = painterResource(
                                    id = R.drawable.pencil_dark_ic_24
                                ),
                                contentDescription = "edit",
                                modifier = pencilModifier
                            )
                        else
                            Image(
                                painter = painterResource(
                                    id = R.drawable.pencil_light_ic_24
                                ),
                                contentDescription = "edit",
                                modifier = pencilModifier

                            )
                        Image(
                            painter = painterResource(
                                id = R.drawable.bin_ic_24
                            ),
                            contentDescription = "delete",
                            modifier = Modifier
                                .padding(10.dp)
                                .size(24.dp)
                                .clickable {
                                    deleteDialog = true
                                }
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChipsPanel(
                    chipsList = productEntry.tags
                )
            }
            Row(
                modifier = Modifier
                    .padding(15.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "На складе",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .padding(end = 30.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = "Дата добавления",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .padding(15.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = productEntry.amount.toString(),
                        fontSize = 16.sp
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .padding(end = 30.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(productEntry.time.toLong()), ZoneId.systemDefault()
                        ).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
    Spacer(
        modifier = Modifier
            .height(15.dp)
    )
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipsPanel(
    chipsList: List<String>
) {
    if (chipsList.isNotEmpty()) {
        FlowRow(
            modifier = Modifier
                .padding(start = 15.dp, end = 5.dp, top = 3.dp, bottom = 3.dp),
        ) {
            for (chip in chipsList) {
                ChipEntry(value = chip)
                Spacer(
                    modifier = Modifier.width(15.dp)
                )
            }
        }
    }
}

@Composable
fun ChipEntry(
    modifier: Modifier = Modifier,
    value: String
) {
    Column {
        Spacer(
            Modifier.height(10.dp)
        )
        Box(
            modifier
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(start = 25.dp, end = 25.dp, top = 8.dp, bottom = 8.dp)
        ) {
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun EditDialog(
    iconModifier: Modifier,
    product: ProductDTO,
    onDismiss: () -> Unit,
    onConfirm: (ProductDTO) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .shadow(5.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight() // Используем wrapContentHeight для автоматического подбора высоты
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween // Используем SpaceBetween для равномерного распределения элементов
            ) {
                var newAmount by remember { mutableIntStateOf(product.amount) }

                if (isSystemInDarkTheme())
                    Image(
                        painter = painterResource(id = R.drawable.settings_dark_ic_24),
                        contentDescription = "edit",
                        modifier = iconModifier
                    )
                else
                    Image(
                        painter = painterResource(id = R.drawable.settings_light_ic_24),
                        contentDescription = "edit",
                        modifier = iconModifier
                    )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Количество товара",
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        border = BorderStroke(5.dp, MaterialTheme.colorScheme.surface),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue),
                        onClick = {
                            if (newAmount > 0)
                                newAmount--
                        }
                    ) {
                        Text(
                            text = "-",
                            fontSize = 48.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(
                        text = newAmount.toString(),
                        fontSize = 48.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    OutlinedButton(
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        border = BorderStroke(5.dp, MaterialTheme.colorScheme.surface),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue),
                        onClick = {
                            newAmount++
                        }
                    ) {
                        Text(
                            text = "+",
                            fontSize = 48.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = "Отменить",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Button(
                        onClick = {
                            onConfirm(product.copy(amount = newAmount))
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = "Принять",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun DeleteDialog(
    iconModifier: Modifier,
    product: ProductDTO,
    onDismiss: () -> Unit,
    onConfirm: (ProductDTO) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .shadow(5.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                var newAmount by remember { mutableIntStateOf(product.amount) }

                if (isSystemInDarkTheme())
                    Image(
                        painter = painterResource(id = R.drawable.warning_light_ic_24),
                        contentDescription = "edit",
                        modifier = iconModifier
                    )
                else
                    Image(
                        painter = painterResource(id = R.drawable.warning_dark_ic_24),
                        contentDescription = "edit",
                        modifier = iconModifier
                    )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Удаление товара",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Вы действительно хотите удалить выбранный товар?",
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "Нет",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Button(
                        onClick = {
                            onConfirm(product.copy(amount = newAmount))
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "Да",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}