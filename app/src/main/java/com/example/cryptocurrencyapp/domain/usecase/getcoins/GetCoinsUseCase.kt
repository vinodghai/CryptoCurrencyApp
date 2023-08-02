package com.example.cryptocurrencyapp.domain.usecase.getcoins

import com.example.cryptocurrencyapp.common.Resource
import com.example.cryptocurrencyapp.data.remote.dto.toCoin
import com.example.cryptocurrencyapp.domain.model.Coin
import com.example.cryptocurrencyapp.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) {

    operator fun invoke(): Flow<Resource<List<Coin>>> = flow {
        emit(Resource.Loading())
        emit(getCoins())
    }

    private suspend fun getCoins(): Resource<List<Coin>> = try {
        val coins = repository.getCoins().map { it.toCoin() }
        Resource.Success(coins)
    } catch (e: HttpException) {
        Resource.Error(e.localizedMessage ?: "An unexpected error occurred!")
    } catch (e: IOException) {
        Resource.Error("Couldn't reach server. Check your internet connection!")
    }
}