# 📱 Supabase Setup in Android (Jetpack Compose)

## 1️⃣ What is `SupabaseClientProvider`?
In our project, the `SupabaseClientProvider` object connects our app to Supabase.  
It stores:
- **Supabase URL** → Your project’s API URL  
- **Supabase Key** → Your project’s API key (public `anon` key)

---

## 2️⃣ Get Your Supabase URL and Key
1. Go to your [Supabase Dashboard](https://supabase.com/dashboard).
2. Open your project.
3. Click **Project Settings → API**.
4. Copy:
   - **Project URL** (starts with `https://`)
   - **anon public key**

---

## 3️⃣ Change URL and Key in `SupabaseClientProvider`
Open **`SupabaseClientProvider.kt`** and replace:

```kotlin
object SupabaseClientProvider {
    val supabase: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://YOUR_PROJECT_URL.supabase.co", // Change this
        supabaseKey = "YOUR_ANON_PUBLIC_KEY" // Change this
    ) {
        install(GoTrue)    // Authentication
        install(Postgrest) // Database
        install(Storage)   // File storage
    }
}
