package com.group4.secondhand.data

import com.group4.secondhand.data.api.ApiHelper
import com.group4.secondhand.data.api.ApiService
import com.group4.secondhand.data.database.MyDatabase
import com.group4.secondhand.data.datastore.UserPreferences
import com.group4.secondhand.data.model.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import retrofit2.Response


class RepositoryTest {

    private val token = "token"
    private val requestApproveOrder = RequestApproveOrder("accepted")
    private val requestUpdateStatusProduk = RequestUpdateStatusProduk("available")
    val multipartBody = mockk<MultipartBody.Part>()
    val requestBody = mockk<RequestBody>()
    val listInt = mockk<List<Int>>()

    //collaborator
    private lateinit var apiService: ApiService
    private lateinit var apiHelper: ApiHelper
    private lateinit var userPreferences: UserPreferences
    private lateinit var database: MyDatabase

    //system under test
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        apiService = mockk()
        apiHelper = ApiHelper(apiService)
        userPreferences = mockk()
        database = mock()
        repository = Repository(apiHelper, userPreferences, database)
    }

    @Test
    fun getBanner(): Unit = runBlocking {
        val responseBanner = mockk<List<ResponseGetBanner>>()

        every {
            runBlocking {
                apiService.getBanner()
            }
        } returns responseBanner

        repository.getBanner()
        verify {
            runBlocking {
                apiService.getBanner()
            }
        }
    }

    @Test
    fun getCategoryHome(): Unit = runBlocking {
        val responseGetCategoryHome = mockk<List<ResponseCategoryHome>>()
        every {
            runBlocking {
                apiService.getCategoryHome()
            }
        } returns responseGetCategoryHome
        repository.getCategoryHome()
        verify {
            runBlocking {
                apiService.getCategoryHome()
            }
        }
    }

    @Test
    fun getSellerProduct(): Unit = runBlocking {
        val responseGetSellerProduct = mockk<List<ResponseSellerProduct>>()
        every {
            runBlocking {
                repository.getSellerProduct(token)
            }
        } returns responseGetSellerProduct

        repository.getSellerProduct(token)
        verify {
            runBlocking {
                repository.getSellerProduct(token)
            }
        }
    }

    @Test
    fun getSellerOrder(): Unit = runBlocking {
        val responseGetSellerOrder = mockk<List<ResponseSellerOrder>>()
        every {
            runBlocking {
                repository.getSellerOrder(token, "")
            }
        } returns responseGetSellerOrder

        repository.getSellerOrder(token, "")
        verify {
            runBlocking {
                repository.getSellerOrder(token, "")
            }
        }
    }

    @Test
    fun getSellerOrderById(): Unit = runBlocking {
        val responseSellerOrderById = mockk<ResponseSellerOrderById>()
        every {
            runBlocking {
                repository.getSellerOrderById(token, 1)
            }
        } returns responseSellerOrderById

        repository.getSellerOrderById(token, 1)

        verify {
            runBlocking {
                repository.getSellerOrderById(token, 1)
            }
        }
    }

    @Test
    fun deleteSellerProduct(): Unit = runBlocking {
        val responseDeleteSellerProduct = mockk<Response<ResponseDeleteSellerProduct>>()
        every {
            runBlocking {
                repository.deleteSellerProduct(token, 1)
            }
        } returns responseDeleteSellerProduct

        repository.deleteSellerProduct(token, 1)

        verify {
            runBlocking {
                repository.deleteSellerProduct(token, 1)
            }
        }

    }

    @Test
    fun approveOrder(): Unit = runBlocking {
        val responseApproveOrder = mockk<ResponseApproveOrder>()
        every {
            runBlocking {
                repository.approveOrder(token, 1, requestApproveOrder)
            }
        } returns responseApproveOrder

        repository.approveOrder(token, 1, requestApproveOrder)

        verify {
            runBlocking {
                repository.approveOrder(token, 1, requestApproveOrder)
            }
        }
    }

    @Test
    fun updateStatusProduk(): Unit = runBlocking {
        val responseUpdateStatusProduk = mockk<Response<ResponseUpdateStatusProduk>>()
        every {
            runBlocking {
                repository.updateStatusProduk(token, 1, requestUpdateStatusProduk)
            }
        } returns responseUpdateStatusProduk

        repository.updateStatusProduk(token, 1, requestUpdateStatusProduk)

        verify {
            runBlocking {
                repository.updateStatusProduk(token, 1, requestUpdateStatusProduk)
            }
        }
    }

    @Test
    fun uploadProduct(): Unit = runBlocking {
        val responseUploadProduct = mockk<Response<ResponseUploadProduct>>()
        every {
            runBlocking {
                repository.uploadProduct(
                    token,
                    multipartBody,
                    requestBody,
                    requestBody,
                    requestBody,
                    listInt,
                    requestBody,
                )
            }
        } returns responseUploadProduct

        repository.uploadProduct(
            token,
            multipartBody,
            requestBody,
            requestBody,
            requestBody,
            listInt,
            requestBody,
        )
        verify {
            runBlocking {
                repository.uploadProduct(
                    token,
                    multipartBody,
                    requestBody,
                    requestBody,
                    requestBody,
                    listInt,
                    requestBody,
                )
            }
        }
    }

    @Test
    fun updateProduct(): Unit = runBlocking {
        val responseUpdateProduct = mockk<ResponseUpdateProduk>()
        every {
            runBlocking {
                repository.updateProduct(
                    token,
                    1,
                    multipartBody,
                    requestBody,
                    requestBody,
                    requestBody,
                    listInt,
                    requestBody
                )
            }
        } returns responseUpdateProduct

        repository.updateProduct(
            token,
            1,
            multipartBody,
            requestBody,
            requestBody,
            requestBody,
            listInt,
            requestBody
        )

        verify {
            runBlocking {
                repository.updateProduct(
                    token,
                    1,
                    multipartBody,
                    requestBody,
                    requestBody,
                    requestBody,
                    listInt,
                    requestBody
                )
            }
        }
    }

    @Test
    fun getProductSearch(): Unit = runBlocking {
        val responseGetProductSearch = mockk<List<ResponseGetProductSearch>>()

        every {
            runBlocking {
                repository.getProductSearch("", "", "", "", "")
            }
        } returns responseGetProductSearch

        repository.getProductSearch("", "", "", "", "")

        verify {
            runBlocking {
                repository.getProductSearch("", "", "", "", "")

            }
        }
    }

    @Test
    fun getProductById(): Unit = runBlocking {
        val responseGetProductById = mockk<Response<ResponseBuyerProductById>>()
        every {
            runBlocking {
                repository.getProductById(1)
            }
        } returns responseGetProductById

        repository.getProductById(1)

        verify {
            runBlocking {
                repository.getProductById(1)
            }
        }

    }

    @Test
    fun buyerOrder(): Unit = runBlocking {
        val responseBuyerOrder = mockk<Response<ResponseBuyerOrder>>()
        val requestBuyerOrder = RequestBuyerOrder(1, 1)
        every {
            runBlocking {
                repository.buyerOrder(token, requestBuyerOrder)
            }
        } returns responseBuyerOrder

        repository.buyerOrder(token, requestBuyerOrder)

        verify {
            runBlocking {
                repository.buyerOrder(token, requestBuyerOrder)
            }
        }
    }

    @Test
    fun getBuyerWishlist(): Unit = runBlocking {
        val responseGetBuyerWishlist = mockk<List<ResponseGetBuyerWishlist>>()
        every {
            runBlocking {
                repository.getBuyerWishlist(token)
            }
        } returns responseGetBuyerWishlist

        repository.getBuyerWishlist(token)

        verify {
            runBlocking {
                repository.getBuyerWishlist(token)
            }
        }

    }

    @Test
    fun getBuyerOrder(): Unit = runBlocking {
        val responseGetBuyerOrder = mockk<List<ResponseGetBuyerOrder>>()
        every {
            runBlocking {
                repository.getBuyerOrder(token)
            }
        } returns responseGetBuyerOrder

        repository.getBuyerOrder(token)

        verify {
            runBlocking {
                repository.getBuyerOrder(token)
            }
        }
    }

    @Test
    fun updateBuyerOrder(): Unit = runBlocking {
        val responseUpdateBuyerOrder = mockk<Response<ResponseUpdateBuyerOrder>>()
        every {
            runBlocking {
                repository.updateBuyerOrder(token, 1, requestBody)
            }
        } returns responseUpdateBuyerOrder

        repository.updateBuyerOrder(token, 1, requestBody)

        verify {
            runBlocking {
                repository.updateBuyerOrder(token, 1, requestBody)
            }
        }
    }

    @Test
    fun deleteBuyerOrder(): Unit = runBlocking {
        val responseDeleteBuyerOrder = mockk<ResponseDeleteBuyerOrder>()
        every {
            runBlocking {
                repository.deleteBuyerOrder(token, 1)
            }
        } returns responseDeleteBuyerOrder

        repository.deleteBuyerOrder(token, 1)

        verify {
            runBlocking {
                repository.deleteBuyerOrder(token, 1)
            }
        }

    }

    @Test
    fun addWishlist(): Unit = runBlocking {
        val responseAddWishlist = mockk<Response<ResponsePostWishlist>>()
        every {
            runBlocking {
                repository.addWishlist(token, requestBody)
            }
        } returns responseAddWishlist

        repository.addWishlist(token, requestBody)

        verify {
            runBlocking {
                repository.addWishlist(token, requestBody)
            }
        }
    }

    @Test
    fun removeWishlist(): Unit = runBlocking {
        val responseRemoveWishlist = mockk<Response<ResponseRemoveWishlist>>()
        every {
            runBlocking {
                repository.removeWishlist(token, 1)
            }
        } returns responseRemoveWishlist

        repository.removeWishlist(token, 1)

        verify {
            runBlocking {
                repository.removeWishlist(token, 1)
            }
        }
    }

    @Test
    fun authRegister(): Unit = runBlocking {
        val responseAuthRegister = mockk<ResponseRegister>()
        val requestRegister = RequestRegister("", "", "")
        every {
            runBlocking {
                repository.authRegister(requestRegister)
            }
        } returns responseAuthRegister

        repository.authRegister(requestRegister)

        verify {
            runBlocking {
                repository.authRegister(requestRegister)
            }
        }
    }

    @Test
    fun authLogin(): Unit = runBlocking {
        val responseAuthLogin = mockk<ResponseLogin>()
        val requestLogin = RequestLogin("", "")
        every {
            runBlocking {
                repository.authLogin(requestLogin)
            }
        } returns responseAuthLogin

        repository.authLogin(requestLogin)

        verify {
            runBlocking {
                repository.authLogin(requestLogin)
            }
        }
    }

    @Test
    fun getDataUser(): Unit = runBlocking {
        val responseGetDataUser = mockk<ResponseGetDataUser>()
        every {
            runBlocking {
                repository.getDataUser(token)
            }
        } returns responseGetDataUser

        repository.getDataUser(token)

        verify {
            runBlocking {
                repository.getDataUser(token)
            }
        }
    }

    @Test
    fun changePassword(): Unit = runBlocking {
        val responseChangePassword = mockk<Response<ResponseChangePassword>>()
        every {
            runBlocking {
                repository.changePassword(token, requestBody, requestBody, requestBody)
            }
        } returns responseChangePassword

        repository.changePassword(token, requestBody, requestBody, requestBody)

        verify {
            runBlocking {
                repository.changePassword(token, requestBody, requestBody, requestBody)
            }
        }
    }

    @Test
    fun updateDataUser(): Unit = runBlocking {
        val responseUpdateUser = mockk<ResponseUpdateUser>()
        every {
            runBlocking {
                repository.updateDataUser(
                    token,
                    multipartBody,
                    requestBody,
                    requestBody,
                    requestBody,
                    requestBody
                )
            }
        } returns responseUpdateUser
        repository.updateDataUser(
            token,
            multipartBody,
            requestBody,
            requestBody,
            requestBody,
            requestBody
        )

        verify {
            runBlocking {
                repository.updateDataUser(
                    token,
                    multipartBody,
                    requestBody,
                    requestBody,
                    requestBody,
                    requestBody
                )
            }
        }
    }

    @Test
    fun getNotification(): Unit = runBlocking {
        val responseGetNotification = mockk<List<ResponseNotification>>()
        every {
            runBlocking {
                repository.getNotification(token)
            }
        } returns responseGetNotification

        repository.getNotification(token)

        verify {
            runBlocking {
                repository.getNotification(token)
            }
        }
    }

    @Test
    fun markReadNotification(): Unit = runBlocking {
        val responseMarkNotification = mockk<Unit>()
        every {
            runBlocking {
                repository.markReadNotification(token, 1)
            }
        } returns responseMarkNotification

        repository.markReadNotification(token, 1)

        verify {
            runBlocking {
                repository.markReadNotification(token, 1)
            }
        }
    }

    @Test
    fun setToken(): Unit = runBlocking {
        val responseSetToken = mockk<Unit>()
        every {
            runBlocking {
                userPreferences.setToken(token)
            }
        } returns responseSetToken

        userPreferences.setToken(token)

        verify {
            runBlocking {
                userPreferences.setToken(token)
            }
        }
    }

    @Test
    fun getToken(): Unit = runBlocking {
        val responseGetToken = mockk<Flow<String>>()
        every {
            runBlocking {
                userPreferences.getToken()
            }
        } returns responseGetToken

        userPreferences.getToken()
        verify {
            runBlocking {
                userPreferences.getToken()
            }
        }
    }

    @Test
    fun deleteToken(): Unit = runBlocking {
        val responseDeleteToken = mockk<Unit>()
        every {
            runBlocking {
                repository.deleteToken()
            }
        } returns responseDeleteToken

        repository.deleteToken()

        verify {
            runBlocking {
                repository.deleteToken()
            }
        }
    }

}