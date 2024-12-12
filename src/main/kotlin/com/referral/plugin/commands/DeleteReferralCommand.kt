package com.referral.plugin.commands

import com.referral.plugin.database.MongoDBHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bson.Document

// Класс удаления рефки по индексу
class DeleteReferralCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player || args.isEmpty()) return false
        val player = sender

        val index = args[0].toIntOrNull()
        if (index == null) {
            player.sendMessage("Неверный индекс")
            return true
        }

        // Получаем список рефок игрока из бд
        val referrals = MongoDBHandler.getPlayerReferrals(player.uniqueId.toString())
        if (index < 1 || index > referrals.size) {
            player.sendMessage("Индекс вне диапазона")
            return true
        }

        val codeToDelete = referrals[index - 1]["code"] as String
        val collection = MongoDBHandler.getCollection("referrals")

        // Удаляем реф код из бд
        collection.deleteOne(Document("code", codeToDelete))
        player.sendMessage("Ваш реферальный код успешно удален.")
        return true
    }
}