package com.example.cryptocurrencyapp.domain.usecase.getcoin

import com.example.cryptocurrencyapp.common.Resource
import com.example.cryptocurrencyapp.data.remote.dto.toCoinDetail
import com.example.cryptocurrencyapp.domain.model.CoinDetail
import com.example.cryptocurrencyapp.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinUseCase @Inject constructor(
    private val repository: CoinRepository
) {

    operator fun invoke(coinId: String): Flow<Resource<CoinDetail>> = flow {
        emit(Resource.Loading())
        emit(getCoinById(coinId))
    }

    private suspend fun getCoinById(coinId: String): Resource<CoinDetail> = try {
        val coin = repository.getCoinById(coinId).toCoinDetail()
        Resource.Success(coin)
    } catch (e: HttpException) {
        Resource.Error(e.localizedMessage ?: "An unexpected error occurred!")
    } catch (e: IOException) {
        Resource.Error("Couldn't reach server. Check your internet connection!")
    }
}