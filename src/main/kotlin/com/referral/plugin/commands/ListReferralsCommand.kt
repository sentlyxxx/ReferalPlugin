package com.referral.plugin.commands

import com.referral.plugin.database.MongoDBHandler
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
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

        if (referrals.isEmpty()) {
            player.sendMessage(Component.text("У вас нет реферальных кодов.").color(NamedTextColor.RED))
        } else {
            player.sendMessage(Component.text("Ваши реферальные коды:").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD))
            referrals.forEachIndexed { index, referral ->
                val code = referral["code"] as String
                val nickname = referral["nickname"] as String
                val expirationTime = referral["expirationTime"] as Long

                val timeLeft = (expirationTime - System.currentTimeMillis()) / 1000
                val timeText = if (timeLeft <= 0) {
                    Component.text("истек").color(NamedTextColor.RED)
                } else {
                    val hours = timeLeft / 3600
                    val minutes = (timeLeft % 3600) / 60
                    Component.text("${hours}ч ${minutes}м").color(NamedTextColor.GRAY)
                }

                val referralMessage = Component.text("#${index + 1} - ")
                    .color(NamedTextColor.WHITE)
                    .append(
                        Component.text(code)
                            .color(NamedTextColor.AQUA)
                            .clickEvent(ClickEvent.copyToClipboard(code)) // Копирование кода
                            .hoverEvent(Component.text("Нажмите, чтобы скопировать код"))
                    )
                    .append(Component.text(" - Игрок: ").color(NamedTextColor.WHITE))
                    .append(Component.text(nickname).color(NamedTextColor.YELLOW))
                    .append(Component.text(" - Истекает через: ").color(NamedTextColor.WHITE))
                    .append(timeText)

                player.sendMessage(referralMessage)
            }
        }
        return true
    }
}
