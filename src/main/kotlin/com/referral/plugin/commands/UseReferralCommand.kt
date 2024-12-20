package com.referral.plugin.commands

import com.referral.plugin.database.MongoDBHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bson.Document

class UseReferralCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player || args.isEmpty()) {
            sender.sendMessage("Использование: /useReferral <код>")
            return false
        }

        val player = sender
        val code = args[0]

        val collection = MongoDBHandler.getCollection("referrals")
        val referral = collection.find(Document("code", code)).firstOrNull()

        if (referral == null) {
            player.sendMessage("Недействительный реферальный код.")
        } else {
            val nickname = referral.getString("nickname") ?: "Неизвестный"
            player.sendMessage("Реферальный код успешно использован! Код был создан игроком $nickname.")
        }

        return true
    }
}
