package com.faith.myapplication.Data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
//import io.github.jan.supabase.gotrue.GoTrue

import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClientProvider {
    val supabase: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://xuupcrlelycousqkabap.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inh1dXBjcmxlbHljb3VzcWthYmFwIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTQ4OTUwMzgsImV4cCI6MjA3MDQ3MTAzOH0.DdHTAYAZOf0-MgQ33Zn2xrDjUEC7Vg9VV9hTALG7Qxo"
    ) {
//        install(GoTrue)
        install(Postgrest)
        install(Storage)
    }
}
