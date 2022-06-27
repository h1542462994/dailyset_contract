# dailyset_contract

English [简体中文(TODO)](./README_cn.md)

**dailyset_contract** is the infrastructure for the [dailyset apps](https://github.com/h1542462994/DailySet). it defines some basic entities in use.
**dailyset_contract** also defines the universal contract for bean in **sync module (centralize)**.

the **sync module** encapsulates common functions to support *data synchronization between multi endpoints*. it is implemented by **usn** (unique sequence number). in contract, usn is defined as **version**, which is managed by server. timestamp is also used to determine the immediacy for the submitted data from client.

otherwise, the contract also supports *local data change caching*. client could submit the data to the server to guarantee the data insistence between client and server. 

the **easy-to-use and the flexibility (extensibility)** are firstly concerned to design the sync module, so we could extend the sync module to other scenarios.

## Features

1. supports data synchronization between multi endpoint, which supports offline synchronization and incremental updates.
2. **non-intrusive**: we provide compat for bean conversion and dao. the data used by sync module and the data to storage could be different.
3. **sync or async**: we provide 2 way to access data, sync way is blocked, and async is not blocked, which is implemented by **kotlin flow / kotlin coroutine**.
4. **easy builder**: we provide some builder to construct the sync module.

to now, **sync** way to access data is completed, **async** way is in the route map.

## Leading Knowledge

1. some **kotlin** coding experience, the contract is fully designed for kotlin language, so it may not be suitable for java applications.
2. kotlin serialization json: kotlin serialization is the *default serializer and deserializer* for the data to transport. if you have some experience on *jackson or gson*, it is not difficult.
3. kotlin flow / kotlin coroutine: kotlin flow is a reactive framework for data. it is likely to rxjava or web-flex. the data is represented as data flow, so it could be subscribed like a data source.

## Implementation

// TODO()

## How to use

when you are prepared to use it, you should define your **entity beans and enums** first.

we use a **vocal** example to presents how to use it, it has two entities: Song and Album.

1. import this library by `build.gradle` or `build.gradle.kts`

**build.gradle**
    
the library is not published to `mavenCentral`, so you should use it by `mavenLocal`.
```groovy
implementation ("org.tty.dailyset:dailyset_contract:$version")
  ```
    
**build.gradle.kts**

```kotlin
implementation ("org.tty.dailyset:dailyset_contract:$version")
  ```

2. define then **enum and entity** used.

**define enums**

`VocalType` is the `ResourceSet::Type`, which to determine the setType.

`VocalContentType` is the `ResourceContent::Type`, which to determine the contentType.

Each of certain `VocalContentType` presents an entity to managed by **sync module**. For example `VocalContentType.Song` is presents an entity `Song`.

*VocalType and VocalContentType could be the persistent type with database (like integer). but we not recommend to use them.*

```kotlin
/**
 * ResourceSet::Type, typed as ES in sync module. to determine the setType.
 */
enum class VocalType {
    Normal,
    Star
}

/**
 * ResourceContent::Type, typed as EC in sync module. to determine the contentType.
 */
enum class VocalContentType {
    Music,
    Album
}
```

**define entity**

we define two entities: `Music` and `Album`, and the parent interface `VocalContent`

`Music` is a basic info of a music. `Album` is a collection of the `Music`, which is a delivery by music publisher or just a set of favor by user. All of them contains the uid(s) of opposite entity.


*The sync module just supported not relational data, so you should manage the relationship manually. relationship maybe (not probably) supported in future versions.*

```kotlin
import kotlinx.serialization.Serializable
import kotlinx.serialization.Polymorphic
import org.tty.dailyset.contract.declare.Key
import org.tty.dailyset.contract.declare.ResourceContent

@Serializable
@Polymorphic
sealed interface VocalContent: ResourceContent

@SerialName("music")
@Serializable
data class Music(
   override val uid: String,
): VocalContent, Key<String> {
    override fun copyByUid(uid: String): ResourceContent {
        return copy(uid = uid)
    }

    override fun key(): String {
        return name
    }
}

@Serializable
@SerialName("album")
data class Album(
    override val uid: String,
    val name: String,
    val songUids: List<String>,
    val description: String
): VocalContent {
    override fun copyByUid(uid: String): ResourceContent {
        return copy(uid = uid)
    }
}
```

3. define the real storage data bean (optional) and the dao compat.

In my case, the real storage data **is equal as** the data, so this part is skipped, for more information, please see *the wiki page (building)*.

The dao compat is *normally* the interface **interact with database**, in my case, it is used by **memory storage.** you could see it in test code, package with `org.tty.dailyset.contract.vocal.memory`.

*the server uses three types of bean: ResourceSet&lt;ES&gt; ResourceLink&lt;EC&gt; TC. in my case, it is VocalType, VocalContentType, VocalContent, respectively.*

*the client uses five types of bean, apart from bean used in server, ResourceTemporaryLink&lt;EC&gt; and ResourceSetVisibility is also needed.*

for more information, please *see the wiki page (building)*.

```kotlin
private val inMemoryResourceSets = InMemoryResourceSets<VocalType>
private val inMemoryResourceLinks = InMemoryResourceLinks<VocalContentType>
private val inMemoryMusics = InMemoryResourceContents<Music>()
private val inMemoryAlbums = InMemoryResourceContents<Album>()
```

4. construct the server and client.

The **sync module** is built by `ResourceSyncBuilderSync/Async` and `Builders`. you should use an **object** to create it.

```kotlin
import org.tty.dailyset.contract.descriptor.*
import org.tty.dailyset.contract.module.resourceSyncClientSync
import org.tty.dailyset.contract.module.resourceSyncServerSync
import org.tty.dailyset.contract.test.vocal.bean.*
import org.tty.dailyset.contract.test.vocal.memory.*

class VocalEngine {
    // define your dao compats.
    val setsServer = InMemoryResourceSets<VocalType>()
    val linksServer = InMemoryResourceLinks<VocalContentType>()
    
    val musicsServer = InMemoryResourceContents<Music>()
    val albumsServer = InMemoryResourceContents<Album>()

    val setsClient = InMemoryResourceSets<VocalType>()
    val linksClient = InMemoryResourceLinks<VocalContentType>()
    val temporaryLinksClient = InMemoryResourceTemporaryLinks<VocalContentType>()
    val musicsClient = InMemoryResourceContents<Music>()
    val albumsClient = InMemoryResourceContents<Album>()
    val setVisibilitiesClient = InMemoryResourceSetVisibilities()
    
    // create the server
    val server = resourceSyncServerSync<VocalContent, VocalType, VocalContentType> { 
        registerSetDescriptor(
            resourceSetDescriptorSync(setsServer)
        )
        registerLinkDescriptor(
            resourceLinkDescriptorSync(linksServer)
        )
        registerContentDescriptors(
            resourceContentDescriptorSync(
                contentType = VocalContentType.Music,
                resourceContentDaoCompatSync = musicsServer
            ),
            resourceContentDescriptorSync(
                contentType = VocalContentType.Album,
                resourceContentDaoCompatSync = albumsServer
            )
        )
    }
    
    val client = resourceSyncClientSync<VocalContent, VocalType, VocalContentType> {
        registerSetDescriptor(
            resourceSetDescriptorSync(setsClient)
        )
        registerLinkDescriptor(
            resourceLinkDescriptorSync(linksClient)
        )
        registerTemporaryLinkDescriptor(
            resourceTemporaryLinkDescriptorSync(temporaryLinksClient)
        )
        registerContentDescriptors(
            resourceContentDescriptorSync(
                contentType = VocalContentType.Music,
                resourceContentDaoCompatSync = musicsClient
            ),
            resourceContentDescriptorSync(
                contentType = VocalContentType.Album,
                resourceContentDaoCompatSync = albumsClient
            )
        )
        registerSetVisibilityDescriptor(
            resourceSetVisibilityDescriptorSync(setVisibilitiesClient)
        )
    }
}
```