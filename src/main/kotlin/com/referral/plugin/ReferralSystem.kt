package com.referral.plugin

import com.referral.plugin.commands.*
import com.referral.plugin.database.MongoDBHandler
import org.bukkit.plugin.java.JavaPlugin

class ReferralSystem : JavaPlugin() {
    override fun onEnable() {
        MongoDBHandler.connect()
        getCommand("createReferral")?.setExecutor(CreateReferralCommand())
        getCommand("deleteReferral")?.setExecutor(DeleteReferralCommand())
        getCommand("listReferrals")?.setExecutor(ListReferralsCommand())
        getCommand("useReferral")?.setExecutor(UseReferralCommand())
    }

    override fun onDisable() {
        MongoDBHandler.disconnect()
    }
}