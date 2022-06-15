package com.group4.secondhand.ui.akun

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
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

        }
    }

}