package com.referral.plugin.commands

import com.referral.plugin.database.MongoDBHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.UUID

class CreateReferralCommand : CommandExecutor, Listener {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Команду может использовать только игрок.")
            return true
        }

        val player: Player = sender
        val code = UUID.randomUUID().toString().substring(0, 6) // Генерация кода длиной 6 символов
        val expirationTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000) // 24 часа в миллисекундах

        MongoDBHandler.saveReferral(player.uniqueId.toString(), code, player.name, expirationTime)

        // Отправка реферального кода в чат
        player.sendMessage("Ваш реферальный код: $code")
        player.sendMessage("Срок действия кода истекает через 24 часа.")

        return true
    }

    @EventHandler
    fun onPlayerClick(event: PlayerInteractEvent) {
        val player = event.player
        val item: ItemStack = player.inventory.itemInMainHand

        if (item.hasItemMeta()) {
            val itemMeta: ItemMeta = item.itemMeta!!
            itemMeta.setCustomModelData(123)
            item.itemMeta = itemMeta
            player.sendMessage("CustomModelData установлен на значение 123!")
        } else {
            player.sendMessage("Этот предмет не имеет мета-данных.")
        }
    }
}
