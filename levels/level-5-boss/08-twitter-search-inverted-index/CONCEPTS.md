# Concepts — Tweet search with an inverted index

Background you need for this problem. It explains *how things work* — it does not solve the problem for you.

## Inverted index

A normal ("forward") index maps document → words. An **inverted index** maps **word → documents** containing it. To find documents containing both "world" and "cup", you intersect two lists instead of reading every document. Every search engine — Google, Elasticsearch, Lucene, Twitter's Earlybird — is built on this.

## Tokenization

Before indexing, text is split into **tokens** and normalised (lowercase, maybe remove punctuation, stem "running" → "run"). The query must be tokenized the **same way**, otherwise "World" wouldn't match "world". Deciding what counts as a token (hashtags? emojis? numbers?) is a product decision.

## Posting lists and intersection

The list of documents for a word is its **posting list**. AND queries intersect posting lists; start with the **shortest** list so you do the least work. Real engines keep postings sorted by id and intersect them like a merge, often skipping ahead with skip lists.

## Positional index and phrase queries

To answer `"world cup"` you need to know **where** each word appears. A **positional index** stores positions per document; a phrase matches if the positions line up (`cup` at `p+1` when `world` is at `p`).

## Deletes and updates

Removing a document from every posting list is expensive at scale. Search engines usually mark it deleted (**tombstone**) and physically remove it later when **segments** are merged. Updates are "delete + re-add".

## Ranking

Matching decides *which* documents qualify; ranking decides the *order*. Here it's simply newest first. Real ranking mixes text relevance (TF-IDF, **BM25**), freshness, engagement and personal signals.
