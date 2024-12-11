package com.referral.plugin.database

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import org.bson.Document

object MongoDBHandler {
    private lateinit var client: MongoClient
    private val databaseName = "referralPlugin"

    fun connect() {
        client = MongoClients.create("mongodb://localhost:27017/")
    }

    fun disconnect() {
        client.close()
    }

    fun getCollection(collectionName: String): MongoCollection<Document> {
        return client.getDatabase(databaseName).getCollection(collectionName)
    }

    fun getPlayerReferrals(playerId: String): List<Document> {
        val collection = getCollection("referrals")
        return collection.find(Document("playerId", playerId)).toList()
    }
}