package com.referral.plugin.commands

import com.referral.plugin.database.MongoDBHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ListReferralsCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        val player = sender
        val referrals = MongoDBHandler.getPlayerReferrals(player.uniqueId.toString())
        if (referrals.isEmpty()) {
            player.sendMessage("У вас нет рефералов.")
        } else {
            player.sendMessage("Ваши рефералы:")
            referrals.forEach { referral ->
                player.sendMessage("Код: ${referral["code"]}")
            }
        }
        return true
    }
}