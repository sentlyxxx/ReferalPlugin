package com.referral.plugin.database

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import org.bson.Document

object MongoDBHandler {
    private lateinit var client: MongoClient
    private val databaseName = "referralPlugin"

    // Подключение к бд
    fun connect() {
        client = MongoClients.create("mongodb://localhost:27017")
    }

    // Отключение от бд
    fun disconnect() {
        client.close()
    }

    // Получение коллекции из базы данных
    fun getCollection(collectionName: String): MongoCollection<Document> {
        return client.getDatabase(databaseName).getCollection(collectionName)
    }

    // Удаление истекших рефералок
    fun cleanExpiredCodes() {
        val collection = getCollection("referrals")
        val currentTime = System.currentTimeMillis()
        collection.deleteMany(Document("expirationTime", Document("\$lt", currentTime)))
    }

    // Получение рефералок игрока
    fun getPlayerReferrals(playerId: String): List<Document> {
        val collection = getCollection("referrals")
        return collection.find(Document("playerId", playerId)).toList()
    }

    // Сохранение рефералки
    fun saveReferral(playerId: String, referralCode: String, playerName: String, expirationTime: Long) {
        val collection = getCollection("referrals")
        val referralDocument = Document()
            .append("playerId", playerId)
            .append("referralCode", referralCode)
            .append("playerName", playerName)
            .append("expirationTime", expirationTime)
        
        collection.insertOne(referralDocument)
    }
}
