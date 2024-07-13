kage com.example.shoppinglist.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun mainlist() {
    var showdialog by remember { mutableStateOf(false) }
    var litems by remember { mutableStateOf(listOf<Shoppingitems>()) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    var dropmenu by remember { mutableStateOf(false) }
    var itemunit by remember { mutableStateOf("Select") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        @Composable
        fun dialogbox() {

            AlertDialog(onDismissRequest = { showdialog = false },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Button(onClick = {
                            if (itemName.isNotBlank() && itemQuantity.isNotBlank()) {
                                var newitem = Shoppingitems(
                                    id = litems.size + 1,
                                    name = itemName,
                                    quantity = itemQuantity.toInt(),
                                    unit = itemunit
                                )
                                litems = litems + newitem
                                itemName = ""
                                itemQuantity = ""
                                showdialog = false
                            }
                        }) {
                            Text(text = "ADD")
                        }
                        Button(onClick = { showdialog = false }) {
                            Text(text = "CANCEL")
                        }
                    }
                },
                title = { Text(text = "Add the Shopping Items") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = itemName,
                            onValueChange = { itemName = it },
                            label = { Text(text = "Name") },
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row {
                            OutlinedTextField(
                                value = itemQuantity,
                                onValueChange = { itemQuantity = it },
                                label = { Text(text = "Quantity") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(0.5f)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Button(
                                onClick = { dropmenu = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(y = 10.dp),
                            ) {
                                Text(text = "$itemunit")
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Arrow Down"
                                )

                                DropdownMenu(expanded = dropmenu,
                                    onDismissRequest = { dropmenu = false }) {

                                    DropdownMenuItem(text = { Text(text = "No Unit") },
                                        onClick = {
                                            itemunit = ""
                                            dropmenu = false
                                        })
                                    DropdownMenuItem(text = { Text(text = "Pieces") },
                                        onClick = {
                                            itemunit = "Pieces"
                                            dropmenu = false
                                        })
                                    DropdownMenuItem(text = { Text(text = "Dozen") },
                                        onClick = {
                                            itemunit = "Dozen"
                                            dropmenu = false
                                        })
                                    DropdownMenuItem(text = { Text(text = "kg") },
                                        onClick = {
                                            itemunit = "kg"
                                            dropmenu = false
                                        })
                                    DropdownMenuItem(text = { Text(text = "gram") },
                                        onClick = {
                                            itemunit = "gram"
                                            dropmenu = false
                                        })
                                    DropdownMenuItem(text = { Text(text = "litre") },
                                        onClick = {
                                            itemunit = "litre"
                                            dropmenu = false
                                        })
                                    DropdownMenuItem(text = { Text(text = "ml") },
                                        onClick = {
                                            itemunit = "ml"
                                            dropmenu = false
                                        })
                                }
                            }

                        }

                    }
                })
        }

        Button(
            onClick = { showdialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add Item")
        }

        if (showdialog) {
            dialogbox()
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            items(litems) {
                item ->
                if (item.isEditing){
                    editor(item = item, onEditComplete = {
                        editedName, editedQuantity ->
                        litems = litems.map { it.copy(isEditing = false) }
                        val editeditem = litems.find { it.id==item.id }
                        editeditem?.let {
                            it.name = editedName
                            it.quantity = editedQuantity
                        }
                    })
                }
                
                else{
                    listColumn(item = item, onEditClick = {
                        litems = litems.map { it.copy(isEditing = it.id==item.id) }
                    },
                        onDeleteClick = {
                            litems = litems-item
                        })
                }
            }
        }
    }
}


@Composable
fun listColumn(
    item: Shoppingitems,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.background),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "${item.name} ", modifier = Modifier.padding(8.dp))

        Spacer(modifier = Modifier.width(22.dp))

        Text(text = "Qty: ${item.quantity} ${item.unit} ", modifier = Modifier.padding(8.dp))

        Spacer(modifier = Modifier.width(35.dp))

        Row() {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}


@Composable
fun editor(item: Shoppingitems, onEditComplete: (String, Int) -> Unit) {
    var editedName by remember{ mutableStateOf(item.name) }
    var editedQuantity by remember{ mutableStateOf(item.quantity.toString()) }
    var Edit by remember{ mutableStateOf(item.isEditing) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(Color.LightGray),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically) {

        Column(modifier = Modifier.padding(5.dp)) {

            BasicTextField(value = editedName,
                onValueChange = {editedName = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))

            BasicTextField(value = editedQuantity,
                onValueChange = {editedQuantity = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))
        }

        Button(onClick = { Edit = false
        onEditComplete(editedName,editedQuantity.toInt())}) {
            Text(text = "Save")
        }


    }
}


data class Shoppingitems(
    val id: Int,
    var name: String,
    var quantity: Int,
    var unit: String,
    var isEditing: Boolean = false
)



