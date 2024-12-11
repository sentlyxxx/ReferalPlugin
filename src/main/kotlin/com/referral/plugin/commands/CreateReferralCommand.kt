package com.referral.plugin.commands

import com.referral.plugin.database.MongoDBHandler
import com.referral.plugin.utils.ReferralCodeGenerator
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bson.Document

class CreateReferralCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        val player = sender
        val code = ReferralCodeGenerator.generateCode()
        val collection = MongoDBHandler.getCollection("referrals")
        collection.insertOne(Document("playerId", player.uniqueId.toString()).append("code", code))
        player.sendMessage("Ваш реферальный код: $code")
        return true
    }
}