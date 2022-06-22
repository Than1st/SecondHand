@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.auth

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
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
import com.group4.secondhand.data.model.RequestLogin
import com.group4.secondhand.ui.theme.MyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginCompose : Fragment() {

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
                            HeaderRegister()
                            ActionItem()

                        }
                    }
                }
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressDialog(requireContext())
        viewModel.login.observe(viewLifecycleOwner) { resources ->
            when (resources.status) {
                Status.LOADING -> {
                    progressDialog.setMessage("Please Wait...")
                    progressDialog.show()
                }
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "Login Success", Toast.LENGTH_SHORT)
                        .show()
                    val token = resources.data?.accessToken
                    if (token != null) {
                        viewModel.setToken(token)
                        Handler().postDelayed({
                            findNavController().navigate(R.id.action_loginCompose_to_homeFragment)
                            progressDialog.dismiss()
                        }, 1000)
                    }
//                    viewModel.login.removeObservers(viewLifecycleOwner)
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), "Username/Password Salah!", Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
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
        onClick: () -> Unit = {
            findNavController().popBackStack()
        }
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
    fun HeaderRegister() {
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
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Image App",
                modifier = Modifier.size(200.dp, 200.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Sign In!",
                fontSize = 16.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }


    @Composable
    fun ActionItem() {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {

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
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Button(
                onClick = {
                    when {
                        email == "" || password == "" -> {
                            AlertDialog.Builder(requireContext())
                                .setTitle("")
                                .setMessage("Username atau Password tidak boleh kosong")
                                .setPositiveButton("Ok") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                        else -> {
                            val requestLogin = RequestLogin(email, password)
                            viewModel.authLogin(requestLogin)
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
                    text = "Sign In",
                    style = TextStyle(Color.White, fontWeight = FontWeight.Bold),
                    fontFamily = poppinsFamily
                )

            }
            Text(
                text = "Belum Punya Akun? Buat",
                fontFamily = poppinsFamily,
                modifier = Modifier.clickable(
                    onClick = {
                        findNavController().navigate(R.id.action_loginCompose_to_registerCompose)
                    }),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                color = Color.DarkGray

            )
        }
    }


//    @Preview(showBackground = true, showSystemUi = true)
//    @Composable
//    fun DefaultPreview() {
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
//                HeaderRegister()
//                ActionItem()
//
//            }
//
//        }
//    }
}

