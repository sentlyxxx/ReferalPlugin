package com.referral.plugin.commands

import com.referral.plugin.database.MongoDBHandler
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.UUID

class CreateReferralCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Команду может использовать только игрок.")
            return true
        }

        val player: Player = sender
        val code = UUID.randomUUID().toString().substring(0, 6) // Генерация кода длиной 6 символов
        val expirationTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000) // 24 часа в миллисекундах

        MongoDBHandler.saveReferral(player.uniqueId.toString(), code, player.name, expirationTime)

        val referralMessage = Component.text("Ваш реферальный код: ")
            .color(NamedTextColor.GREEN)
            .append(
                Component.text(code)
                    .color(NamedTextColor.AQUA)
                    .clickEvent(ClickEvent.copyToClipboard(code))
                    .hoverEvent(Component.text("Нажмите, чтобы скопировать код"))
            )

        player.sendMessage(referralMessage)
        player.sendMessage(Component.text("Срок действия кода истекает через 24 часа.").color(NamedTextColor.GRAY))
        return true
    }
}
