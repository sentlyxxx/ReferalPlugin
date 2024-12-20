package com.referral.plugin.commands

import com.referral.plugin.database.MongoDBHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ListReferralsCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Команду может использовать только игрок.")
            return true
        }

        val player: Player = sender
        val referrals = MongoDBHandler.getPlayerReferrals(player.uniqueId.toString())

        if (referrals.isNullOrEmpty()) {
            player.sendMessage("У вас нет реферальных кодов.")
        } else {
            player.sendMessage("Ваши реферальные коды:")
            referrals.forEachIndexed { index, referral ->
                val code = referral["code"] as? String ?: "Неизвестный код"
                val nickname = referral["nickname"] as? String ?: "Неизвестный игрок"
                val expirationTime = referral["expirationTime"] as? Long ?: 0L

                val timeLeft = (expirationTime - System.currentTimeMillis()) / 1000
                val timeText = if (timeLeft <= 0) {
                    "истек"
                } else {
                    val hours = timeLeft / 3600
                    val minutes = (timeLeft % 3600) / 60
                    "${hours}ч ${minutes}м"
                }

                val referralMessage = "#${index + 1} - Код: $code - Игрок: $nickname - Истекает через: $timeText"
                player.sendMessage(referralMessage)
            }
        }
        return true
    }
}