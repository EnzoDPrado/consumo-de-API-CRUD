package com.example.crudclass

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.crudclass.api.ContactCall
import com.example.crudclass.api.RetrofitApi
import com.example.crudclass.model.Contact
import com.example.crudclass.ui.theme.CrudClassTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CrudClassTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {

    var context = LocalContext.current

    var nameState by remember {
        mutableStateOf("")
    }

    var emailState by remember {
        mutableStateOf("")
    }

    var phoneState by remember {
        mutableStateOf("")
    }

    var activeState by remember {
        mutableStateOf(true)
    }


    val retrofit = RetrofitApi.getRetrofit()
    val contactsCall = retrofit.create(ContactCall::class.java)
    val call = contactsCall.getAll()

    var contacts by remember {
        mutableStateOf(listOf<Contact>())
    }

    //Executar a chamada para o endpoint
    call.enqueue(object : Callback<List<Contact>> {
        override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
            Log.i("ds3m", response.body()!!.toString())
            contacts = response.body()!!
        }

        override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
            Log.i("ds3m", t.message.toString())
        }

    })


    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Cadastro de contatos")
        OutlinedTextField(
            value = nameState,
            onValueChange = { nameState = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Contact Name")
            }
        )

        OutlinedTextField(
            value = emailState,
            onValueChange = { emailState = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Contact Email")
            }
        )

        OutlinedTextField(
            value = phoneState,
            onValueChange = { phoneState = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Contact Phone")
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment =Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Checkbox(checked = activeState, onCheckedChange = { activeState = it })

        }
        Button(modifier = Modifier.fillMaxWidth(),onClick = {
            val contact = Contact(
                name = nameState,
                email = emailState,
                phone = phoneState,
                active = activeState
            )
            val callContactPost = contactsCall.save(contact)
            callContactPost.enqueue(object : Callback<Contact> {
                override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
                    Log.i("ds3m", response.body()!!.toString())
                }

                override fun onFailure(call: Call<Contact>, t: Throwable) {
                    Log.i("ds3m", t.message.toString())
                }
            })

        }) {
            Text(text = "Criar um novo usuario")

        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(contacts) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp)
                        .background(Color.DarkGray)
                        .clickable {
                            nameState = it.name
                            emailState = it.email
                            phoneState = it.phone
                            activeState = it.active
                        },

                    ) {
                    Column() {
                        Text(text = it.name)
                        Text(text = it.email)
                        Text(text = it.phone)
                        
                        Button(onClick = {
                            val callContactDelete = contactsCall.delete(id = it.id)
                            callContactDelete.enqueue(object: Callback<String>{
                                override fun onResponse(
                                    call: Call<String>,
                                    response: Response<String>
                                ) {
                                   Toast.makeText(context, response.code().toString(),Toast.LENGTH_SHORT).show()
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    TODO("Not yet implemented")
                                }

                            })

                        }) {
                            Text(text = "Delete")
                        }
                    }
                }
            }

        }

    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CrudClassTheme {
        Greeting("Android")
    }
}