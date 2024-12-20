package com.referral.plugin.commands

import com.referral.plugin.database.MongoDBHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bson.Document

class DeleteReferralCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player || args.isEmpty()) {
            sender?.sendMessage("Использование: /deleteReferral <индекс>")
            return false
        }

        val player = sender
        val index = args[0].toIntOrNull()

        if (index == null || index < 1) {
            player.sendMessage("Укажите корректный индекс реферального кода.")
            return true
        }

        val referrals = MongoDBHandler.getPlayerReferrals(player.uniqueId.toString())
        if (index > referrals.size) {
            player.sendMessage("Индекс вне диапазона. У вас есть только ${referrals.size} реферальных кодов.")
            return true
        }

        val codeToDelete = referrals[index - 1]["code"] as String
        val collection = MongoDBHandler.getCollection("referrals")

        // Удаляем реферальный код
        collection.deleteOne(Document("code", codeToDelete))
        player.sendMessage("Реферальный код с индексом $index успешно удалён.")
        return true
    }
}
