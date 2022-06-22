@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.auth

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status
import com.group4.secondhand.data.model.RequestRegister
import com.group4.secondhand.ui.theme.MyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterCompose : Fragment() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            val progressDialog = ProgressDialog(requireContext())
            viewModel.register.observe(viewLifecycleOwner) { resources ->
                when (resources.status) {
                    Status.LOADING -> {
                        progressDialog.setMessage("Please Wait...")
                        progressDialog.show()
                    }
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), "Register Success", Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.dismiss()
                        findNavController().navigate(R.id.action_registerCompose_to_loginCompose)
                    }
                    Status.ERROR -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Pesan")
                            .setMessage(resources.message)
                            .setPositiveButton("Ok") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
            }
            setContent {
                MyTheme {
                    Surface(

                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        ImageWithBackground(
                            painter = painterResource(id = R.drawable.wallpapers),
                            backgroundDrawableResId = R.drawable.wallpapers,
                            contentDescription = "",
                            modifier = Modifier
                                .height(2580.dp)
                                .width(2960.dp)
                                .padding(0.dp)
                        )
                        Column {
                            HeaderLogin()
                            ActionItem()

                        }
                    }
                }
            }

        }
    }

    private val poppinsFamily = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_semibold, FontWeight.Medium)
    )


    @Composable
    fun ImageWithBackground(
        painter: Painter,
        @DrawableRes backgroundDrawableResId: Int,
        contentDescription: String?,
        modifier: Modifier = Modifier,
        alignment: Alignment = Alignment.Center,
        contentScale: ContentScale = ContentScale.FillBounds,
        alpha: Float = DefaultAlpha,
        colorFilter: ColorFilter? = null
    ) {
        Box(
            modifier = modifier
        ) {
            Image(
                painter = painterResource(backgroundDrawableResId),
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
            )
            Image(
                painter = painter,
                contentDescription = contentDescription,
                alignment = alignment,
                contentScale = contentScale,
                alpha = alpha,
                colorFilter = colorFilter,
                modifier = Modifier
                    .matchParentSize()
            )
        }
    }


    @Composable
    fun CircularButton(
        @DrawableRes iconResouce: Int,
        color: Color = Color.Gray,
        elevation: ButtonElevation? = ButtonDefaults.elevation(),
        onClick: () -> Unit = {}
    ) {
        Button(
            onClick = onClick,
            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = color
            ),
            shape = RoundedCornerShape(22.dp),
            elevation = elevation,
            modifier = Modifier
                .width(26.dp)
                .height(26.dp)
        ) {
            Icon(painterResource(id = iconResouce), null)
        }
    }

    @Composable
    fun HeaderLogin() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(30.dp)
                .padding(horizontal = 16.dp)
        ) {
            CircularButton(R.drawable.ic_back_icon)

        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Text(
//                text = "Second Hand",
//                fontSize = 40.sp,
//                style = TextStyle(
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 16.sp,
//                    fontFamily = poppinsFamily),
//                color = Color.DarkGray
//            )
            Spacer(modifier = Modifier.height(30.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Image App",
                modifier = Modifier.size(200.dp, 200.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Sign Up!",
                fontSize = 16.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold
            )
        }
    }


    @Composable
    fun ActionItem() {

        var username by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }

        var password by remember { mutableStateOf("") }
        var confPassword by remember { mutableStateOf("") }

        var passwordVisibility by remember { mutableStateOf(false) }
        var confirmPasswordVisibility by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {


            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text(text = "Username", fontFamily = poppinsFamily) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp)
            )


            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text(text = "Email", fontFamily = poppinsFamily) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text(text = "password", fontFamily = poppinsFamily) },
//                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(
                            imageVector = if (passwordVisibility)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff, ""
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confPassword,
                onValueChange = { confPassword = it },
                placeholder = { Text(text = "Confirm Password", fontFamily = poppinsFamily) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp),
                visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        confirmPasswordVisibility = !confirmPasswordVisibility
                    }) {
                        Icon(
                            imageVector = if (confirmPasswordVisibility)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff, ""
                        )
                    }
                })

        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    when {
                        username == "" || email == "" || password == "" || confPassword == "" -> {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Pesan")
                                .setMessage("Semua kolom harus diisi")
                                .setPositiveButton("Ok") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                        password != confPassword -> {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Pesan")
                                .setMessage("Password tidak sesuai")
                                .setPositiveButton("Ok") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                        password.length < 6 -> {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Pesan")
                                .setMessage("Password harus lebih dari 6 huruf")
                                .setPositiveButton("Ok") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                        else -> {
                            val requestRegister = RequestRegister(username, email, password)
                            viewModel.authRegister(requestRegister)
                        }
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_blue)),
                shape = RoundedCornerShape(20.dp)
            )


            {
                Text(
                    text = "Sign Up!",
                    style = TextStyle(Color.White, fontWeight = FontWeight.Bold),
                    fontFamily = poppinsFamily
                )

            }
            Text(
                text = "Sudah Punya Akun?",
                fontFamily = poppinsFamily,
                modifier = Modifier.clickable(
                    onClick = {
                        findNavController().navigate(R.id.action_registerCompose_to_loginCompose)
                    }),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                color = Color.DarkGray

            )
        }


    }


//    @Preview(showBackground = true, showSystemUi = true)
//    @Composable
//    fun DefaultPreview() {
//
//        MyTheme {
//
//            ImageWithBackground(
//                painter = painterResource(id = R.drawable.wallpapers),
//                backgroundDrawableResId = R.drawable.wallpapers,
//                contentDescription = "",
//                modifier = Modifier
//                    .height(2580.dp)
//                    .width(2960.dp)
//                    .padding(0.dp),
//            )
//            Column {
//                HeaderLogin()
//                ActionItem()
//
//            }
//
//        }
//    }
}