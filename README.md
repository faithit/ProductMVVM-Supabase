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
```
## 4️⃣ Confirm Your Bucket Name

When uploading images, our code uses the bucket "product-images":

supabase.storage.from("product-images")

✅ Before running the app:

    Go to Storage in Supabase Dashboard.

    Make sure a bucket named product-images exists.

    If your bucket name is different, update it in all places in the code.
## 5️⃣ Enable Kotlin Serialization in Gradle

Supabase Kotlin uses Kotlinx Serialization to send/receive data.

If using Kotlin DSL (build.gradle.kts):

plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0" // Match your Kotlin version
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
}
3️## 6️⃣ Add a Custom Policy (myPolicy)

To allow insert, delete, update, and select operations for the bucket:

    Go to Supabase Dashboard → Storage → Policies.

    Select your bucket (product-images).

    Create a new policy named myPolicy.

    For Each Operation:

        INSERT

        UPDATE

        DELETE

        SELECT

    Allow true for all roles or use:

create policy "myPolicy"
on storage.objects for all
using (true)
with check (true);

    Save and enable the policy.

⚠️ This is for development purposes — in production, make the policy more secure.

## 7️⃣ Enable RLS (Row Level Security) on products Table

Supabase requires Row Level Security (RLS) to be enabled before policies can work.

    Go to Supabase Dashboard → Table Editor.

    Select your products table.

    Click the "RLS" tab.

    Turn Row Level Security ON.

    Make sure you have created and enabled your custom policy (mypolicy) that allows INSERT, SELECT, UPDATE, DELETE.
