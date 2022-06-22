@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.akun

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status.*
import com.group4.secondhand.data.datastore.UserPreferences.Companion.DEFAULT_TOKEN
import com.group4.secondhand.databinding.FragmentAkunBinding
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AkunFragment : Fragment() {

    private var _binding: FragmentAkunBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AkunViewModel by viewModels()
    private val bundle = Bundle()

    companion object {
        const val USER_NAME = "fullname"
        const val USER_EMAIL = "email"
        const val USER_PHONE_NUMBER = "phone_number"
        const val USER_ADDRESS = "address"
        const val USER_CITY = "city"
        const val USER_IMAGE = "image_url"
        const val USER_TOKEN = "user_token"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAkunBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return ComposeView(requireContext()).apply {
//            layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            )
//            setContent {
//                AkunCompose()
//            }
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressDialog(requireContext())
        viewModel.getToken()
        viewModel.alreadyLogin.observe(viewLifecycleOwner) {
            if (it == DEFAULT_TOKEN) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Pesan")
                    .setMessage("Anda Belom Masuk")
                    .setPositiveButton("Login") { dialogP, _ ->
                        findNavController().navigate(R.id.action_akunFragment_to_loginCompose)
                        dialogP.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialogN, _ ->
                        findNavController().navigate(R.id.action_akunFragment_to_homeFragment)
                        dialogN.dismiss()
                    }
                    .setCancelable(false)
                    .show()
                viewModel.alreadyLogin.removeObservers(viewLifecycleOwner)
            } else {
                bundle.putString(USER_TOKEN, it)
                viewModel.getUserData(it)
            }
        }

        viewModel.user.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    progressDialog.dismiss()
                    if (it.data != null) {
                        bundle.putString(USER_NAME, it.data.fullName)
                        bundle.putString(USER_EMAIL, it.data.email)
                        bundle.putString(USER_CITY, it.data.city)
                        bundle.putString(USER_ADDRESS, it.data.address)
                        bundle.putString(USER_PHONE_NUMBER, it.data.phoneNumber)
                        if (it.data.imageUrl != null) {
                            Glide.with(requireContext())
                                .load(it.data.imageUrl.toString())
                                .transform(CenterCrop(), RoundedCorners(12))
                                .into(binding.ivProfile)
                            bundle.putString(USER_IMAGE, it.data.imageUrl.toString() ?: "no_image")
                        }
                    }
                }
                ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                }
                LOADING -> {
                    progressDialog.setMessage("Please Wait...")
                    progressDialog.show()
                }
            }
        }

        binding.listUbahAkun.setOnClickListener {
            findNavController().navigate(R.id.action_akunFragment_to_editAkunFragment, bundle)
        }
        binding.listPengaturanAkun.setOnClickListener {
            Toast.makeText(requireContext(), "Coming Soon!", Toast.LENGTH_SHORT).show()
        }
        binding.listKeluar.setOnClickListener {
            AlertDialog
                .Builder(requireContext())
                .setTitle("Konfirmasi Keluar")
                .setMessage("Yakin ingin keluar?")
                .setPositiveButton("Iya") { dialogPositive, _ ->
                    viewModel.deleteToken()
                    Toast
                        .makeText(
                            requireContext(),
                            "Logout Success",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                    findNavController().navigate(R.id.action_akunFragment_to_homeFragment)
                    dialogPositive.dismiss()
                }
                .setNegativeButton("Tidak") { dialogNegative, _ ->
                    dialogNegative.dismiss()
                }
                .setCancelable(false)
                .show()
        }
    }

//    private val poppinsFamily = FontFamily(
//        Font(R.font.poppins_bold, FontWeight.Bold),
//        Font(R.font.poppins_regular, FontWeight.Normal)
//    )

//    @Composable
//    fun AkunCompose() {
//        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
//            val (title, imageProfile, btnGroup, versionText) = createRefs()
//            Text(
//                text = "Akun Saya",
//                style = TextStyle(
//                    fontFamily = poppinsFamily,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 20.sp
//                ),
//                modifier = Modifier
//                    .padding(start = 32.dp, top = 32.dp)
//                    .constrainAs(title) {
//                        top.linkTo(parent.top)
//                        start.linkTo(parent.start)
//                    }
//            )
//            GlideImage(
//                imageModel = imageUrl,
//                contentScale = ContentScale.Crop,
//                placeHolder = ImageBitmap.imageResource(R.drawable.default_image),
//                error = ImageBitmap.imageResource(R.drawable.default_image),
//                modifier = Modifier
//                    .padding(top = 98.dp)
//                    .size(width = 128.dp, height = 128.dp)
//                    .clip(RoundedCornerShape(8.dp))
//                    .constrainAs(imageProfile) {
//                        top.linkTo(parent.top)
//                        end.linkTo(parent.end)
//                        start.linkTo(parent.start)
//                    }
//            )
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 32.dp)
//                    .constrainAs(btnGroup) {
//                        top.linkTo(imageProfile.bottom)
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                    }
//            ) {
//                // Ubah Akun Button
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable {
//                            findNavController().navigate(R.id.action_akunFragment_to_editAkunFragment, bundle)
//                        }
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_edit),
//                        contentDescription = "",
//                        modifier = Modifier
//                            .padding(top = 12.dp, bottom = 12.dp, start = 27.dp, end = 0.dp)
//                            .size(width = 32.dp, height = 32.dp)
//                    )
//                    Text(
//                        text = "Ubah Akun",
//                        style = TextStyle(
//                            fontFamily = poppinsFamily,
//                            fontWeight = FontWeight.Medium,
//                            fontSize = 16.sp
//                        ),
//                        modifier = Modifier
//                            .padding(top = 16.dp, bottom = 16.dp, end = 32.dp, start = 20.dp)
//                    )
//                }
//                Divider(
//                    color = Color.Gray,
//                    thickness = 1.dp,
//                    modifier = Modifier.padding(horizontal = 27.dp)
//                )
//                // Pengaturan Akun Button
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable {
//                            Toast
//                                .makeText(
//                                    requireContext(),
//                                    "Anda Mencet Pengaturan Akun",
//                                    Toast.LENGTH_SHORT
//                                )
//                                .show()
//                        }
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_setting),
//                        contentDescription = "",
//                        modifier = Modifier
//                            .padding(top = 12.dp, bottom = 12.dp, start = 27.dp, end = 0.dp)
//                            .size(width = 32.dp, height = 32.dp)
//                    )
//                    Text(
//                        text = "Pengaturan Akun",
//                        style = TextStyle(
//                            fontFamily = poppinsFamily,
//                            fontWeight = FontWeight.Medium,
//                            fontSize = 16.sp
//                        ),
//                        modifier = Modifier
//                            .padding(top = 16.dp, bottom = 16.dp, end = 32.dp, start = 20.dp)
//                    )
//                }
//                Divider(
//                    color = Color.Gray,
//                    thickness = 1.dp,
//                    modifier = Modifier.padding(horizontal = 27.dp)
//                )
//                // Logout Button
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable {
//                            AlertDialog
//                                .Builder(requireContext())
//                                .setTitle("Konfirmasi Keluar")
//                                .setMessage("Yakin ingin keluar?")
//                                .setPositiveButton("Iya") { dialogPositive, _ ->
//                                    viewModel.deleteToken()
//                                    Toast
//                                        .makeText(
//                                            requireContext(),
//                                            "Logout Success",
//                                            Toast.LENGTH_SHORT
//                                        )
//                                        .show()
//                                    findNavController().navigate(R.id.action_akunFragment_to_homeFragment)
//                                    dialogPositive.dismiss()
//                                }
//                                .setNegativeButton("Tidak") { dialogNegative, _ ->
//                                    dialogNegative.dismiss()
//                                }
//                                .setCancelable(false)
//                                .show()
//                        }
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_logout),
//                        contentDescription = "",
//                        modifier = Modifier
//                            .padding(top = 12.dp, bottom = 12.dp, start = 27.dp, end = 0.dp)
//                            .size(width = 32.dp, height = 32.dp)
//                    )
//                    Text(
//                        text = "Keluar",
//                        style = TextStyle(
//                            fontFamily = poppinsFamily,
//                            fontWeight = FontWeight.Medium,
//                            fontSize = 16.sp
//                        ),
//                        modifier = Modifier
//                            .padding(top = 16.dp, bottom = 16.dp, end = 32.dp, start = 20.dp)
//                    )
//                }
//                Divider(
//                    color = Color.Gray,
//                    thickness = 1.dp,
//                    modifier = Modifier.padding(horizontal = 27.dp)
//                )
//            }
//            Text(text = "Version 1.0.0",
//                style = TextStyle(
//                    fontFamily = poppinsFamily,
//                    fontWeight = FontWeight.Thin,
//                    fontSize = 12.sp,
//                    color = Color.Gray
//                ),
//                modifier = Modifier
//                    .padding(top = 16.dp)
//                    .constrainAs(versionText) {
//                        top.linkTo(btnGroup.bottom)
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                    })
//        }
//    }
}