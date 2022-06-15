package com.group4.secondhand.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.group4.secondhand.R

class FirstOnBoardingFragment : Fragment() {

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
                FirstOnBoarding()
            }
        }
    }

    private val poppinsFamily = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_regular, FontWeight.Normal)
    )

    @Composable
    fun FirstOnBoarding() {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val(image, fade, title, subTitle, btnNext) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.onboarding_pict1),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                contentScale = ContentScale.Crop
            )
            Image(
                painter = painterResource(id = R.drawable.fade),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(fade) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            Text(
                text = "Mudah",
                style = TextStyle(
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ),
                modifier = Modifier.constrainAs(title){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            )
            Text(
                text = "Jual barang bekas kamu jadi lebih mudah",
                style = TextStyle(
                    fontFamily = poppinsFamily,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .constrainAs(subTitle) {
                        top.linkTo(title.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            Row(
                modifier = Modifier
                    .padding(end = 12.dp, bottom = 12.dp)
                    .clickable {
                        findNavController().navigate(R.id.action_firstOnBoardingFragment_to_secondOnBoardingFragment)
                    }
                    .constrainAs(btnNext) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            ) {
                Text(
                    text = "Selanjutnya",
                    style = TextStyle(
                        fontFamily = poppinsFamily,
                        textAlign = TextAlign.Center
                    )
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_chevron_right),
                    contentDescription = ""
                )
            }
        }
    }

}