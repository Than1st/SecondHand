package com.group4.secondhand.ui.akun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import com.group4.secondhand.R

class AkunFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setContent {
                AkunCompose()
            }
        }
    }

    private val poppinsFamily = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_regular, FontWeight.Normal)
    )

    @Composable
    fun AkunCompose() {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (title, imageProfile, btnGroup, versionText) = createRefs()
            Text(
                text = "Akun Saya",
                style = TextStyle(
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                modifier = Modifier
                    .padding(start = 32.dp, top = 32.dp)
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )
            Image(
                painter = painterResource(id = R.drawable.image_profile),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 98.dp)
                    .size(width = 128.dp, height = 128.dp)
                    .constrainAs(imageProfile) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .constrainAs(btnGroup) {
                        top.linkTo(imageProfile.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                // Ubah Akun Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Toast
                                .makeText(
                                    requireContext(),
                                    "Anda Mencet Ubah Akun",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 12.dp, start = 27.dp, end = 0.dp)
                            .size(width = 32.dp, height = 32.dp)
                    )
                    Text(
                        text = "Ubah Akun",
                        style = TextStyle(
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp, end = 32.dp, start = 20.dp)
                    )
                }
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 27.dp)
                )
                // Pengaturan Akun Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Toast
                                .makeText(
                                    requireContext(),
                                    "Anda Mencet Pengaturan Akun",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_setting),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 12.dp, start = 27.dp, end = 0.dp)
                            .size(width = 32.dp, height = 32.dp)
                    )
                    Text(
                        text = "Pengaturan Akun",
                        style = TextStyle(
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp, end = 32.dp, start = 20.dp)
                    )
                }
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 27.dp)
                )
                // Logout Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Toast
                                .makeText(
                                    requireContext(),
                                    "Anda Mencet Logout",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_logout),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 12.dp, start = 27.dp, end = 0.dp)
                            .size(width = 32.dp, height = 32.dp)
                    )
                    Text(
                        text = "Keluar",
                        style = TextStyle(
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp, end = 32.dp, start = 20.dp)
                    )
                }
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 27.dp)
                )
            }
            Text(text = "Version 1.0.0",
                style = TextStyle(
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Thin,
                    fontSize = 12.sp,
                    color = Color.Gray
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .constrainAs(versionText) {
                        top.linkTo(btnGroup.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
        }
    }

}