package com.referral.plugin.commands

import com.referral.plugin.database.MongoDBHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bson.Document

class DeleteReferralCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        val player = sender
        val collection = MongoDBHandler.getCollection("referrals")
        collection.deleteOne(Document("playerId", player.uniqueId.toString()))
        player.sendMessage("Ваш реферальный код был удалён.")
        return true
    }
}