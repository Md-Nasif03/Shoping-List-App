package com.example.shopinglist


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopinglist.ui.theme.ShopingListTheme

@Composable
fun ShopingList() {
    var sItems by remember {
        mutableStateOf(listOf<Details>())
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    var itemName by remember {
        mutableStateOf("")
    }
    var itemQty by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            showDialog=true
        },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(5.dp)
        ) {
            Text(text = "+ Add item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(sItems){item->
                if (item.isEdit){
                    Editor(Edit_details = item, save = {
                            editedName,editedQty->                        // this are the two parameter that asign to editName and editQty
                        sItems=sItems.map { it.copy(isEdit = false) }
                        val editeditem=sItems.find { it.id==item.id }
                        editeditem?.let {
                            it.name=editedName
                            it.qty=editedQty
                        }
                    })
                }else{
                    Design(details = item, onEdit = {
                        sItems=sItems.map { it.copy(isEdit = (it.id==item.id)) }
                    },
                        onDelete ={
                            sItems=sItems-item
                        })
                }

            }
        }
    }
    val hide=LocalSoftwareKeyboardController.current
    if (showDialog){
        AlertDialog(onDismissRequest = { showDialog=false},
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        hide?.hide()
                        showDialog=false
                        if(itemName.isNotBlank()){
                            val newItem=Details(
                                sItems.size+1,
                                itemName,
                                itemQty.toInt(),
                            )
                            sItems+=newItem
                            itemName=""
                            itemQty=""
                        }
                    }) {
                        Text(text = "Add")
                    }
                    Button(onClick = {
                        hide?.hide()
                        showDialog=false
                    }) {
                        Text(text = "Cancel")
                    }
                }
            },
            title = { Text(text = "Add Shoping item")},
            text = {
                Column {
                    OutlinedTextField(value = itemName,
                        onValueChange = {itemName=it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                    OutlinedTextField(value = itemQty,
                        onValueChange ={itemQty=it},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType=KeyboardType.Number,
                            imeAction = ImeAction.Done
                        )
                    )
                }
            }
        )
    }
}


@Composable
fun Design(
    details: Details,
    onEdit:()->Unit,
    onDelete:()->Unit
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .border(
                border = BorderStroke(2.dp, Color.Cyan),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row (
            modifier = Modifier.wrapContentSize()
        ){
            Text(text = details.id.toString()+". ",
                modifier = Modifier.padding(start = 5.dp,end = 5.dp)
            )
            Text(text = details.name)
        }
        Text(text = "Qty: ${details.qty}")
        Row (
            modifier = Modifier.wrapContentSize()
        ){
            IconButton(onClick = onEdit) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDelete,
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }

        }
    }
}

@Composable
fun Editor(
    Edit_details: Details,
    save:(String,Int)->Unit
){
    var editName by remember {
        mutableStateOf(Edit_details.name)
    }
    var editQty by remember {
        mutableStateOf(Edit_details.qty.toString())
    }
    var editComplete by remember {
        mutableStateOf(Edit_details.isEdit)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            BasicTextField(value = editName,
                onValueChange = {editName=it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            BasicTextField(value = editQty,
                onValueChange = {editQty=it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
        }
        Button(onClick = {
            editComplete=false
            save(editName,editQty.toInt()?:1)
        } ) {
            Text(text = "Save")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ShopingListPreview(){
    ShopingListTheme {
        ShopingList()
    }
}
