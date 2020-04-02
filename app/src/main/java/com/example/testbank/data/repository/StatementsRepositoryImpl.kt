package com.example.testbank.data.repository

import com.example.testbank.data.local.StatementDao
import com.example.testbank.data.remote.RemoteService
import com.example.testbank.domain.models.Statement
import com.example.testbank.domain.repository.StatementsRepository
import com.example.testbank.extensions.withDefaultSchedulers
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class StatementsRepositoryImpl @Inject
constructor(
    private val statementDao: StatementDao,
    private val remoteService: RemoteService
) : StatementsRepository {

    override fun getStatements(idUser: String): Single<List<Statement>> {
            return remoteService.getStatements(idUser)
                .withDefaultSchedulers()
                .map { response ->
                Timber.d("myreponse",response.toString())
                statementDao.insertAllStatements(response.statementList)
                response.statementList.map {
                    Statement(
                        it.date,
                        it.desc,
                        it.title,
                        it.value
                    )
                }
            }
    }
}

