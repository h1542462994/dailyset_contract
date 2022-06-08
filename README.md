# dailyset_contract

English [Chinese Simplified](./README_cn.md)

**dailyset_contract** is the infrastructure for the [dailyset apps](https://github.com/h1542462994/DailySet). it defines some basic contract for entities.

certainly, **dailyset_contract** defines the universal contract for bean in **sync module** (centralized). 

the **sync module** encapsulates common functions to support *data synchronization between multi endpoints*, which supports offline synchronization. it is implemented by usn (unique sequence number). in contract, usn is defined as version number, which is managed by server. timestamp is also used to determine the immediacy for the submitted data.

otherwise, the contract also supports local data change caching. client could submit the data to the server to guarantee the data insistence between client and server. 

the easy-to-use and the flexibility (extensibility) are firstly concerned to design the sync module, so we could extend the sync module to other scenarios.

## Features

1. supports data synchronization between multi endpoint, which supports offline synchronization and incremental updates.
2. non-intrusive: we provide compat for bean conversion and dao. the data used by sync module and the data to storage could be different.
3. sync or async: we provide 2 way to access data, sync way is blocked, and async is not blocked, which is implemented by kotlin flow.
4. easy builder: we provide some builder to construct the sync module.

## Leading Knowledge

1. some kotlin coding experience, the contract is fully designed for kotlin language, so it may not be suitable for java applications.
2. kotlin serialization json: kotlin serialization is the serializer or deserializer for the data to transport. if you have some experience on *jackson or gson*, it is not difficult.
3. kotlin flow: kotlin flow is a reactive framework for data. it is likely to rxjava or web-flex. the data is represented as data flow, so it could be subscribed like a data source.

## Implementation

// TODO()

## How to use

// TODO()