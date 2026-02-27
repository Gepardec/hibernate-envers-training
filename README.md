# 📚 Bookshop — Hibernate Envers Training

Welcome to the **Bookshop Envers Training** 🚀  
A hands-on journey into **Hibernate Envers** with **Quarkus**.

Each package under:

```
src/test/java/com/gepardec/bookshop/examples/
```

contains a **self-contained exercise**.

You will:

1. ▶️ Run the tests
2. 🔴 Watch them fail
3. 🛠 Implement the missing Envers feature
4. 🟢 Turn everything green

You are not just reading history — you are **writing it**.

---

## 🧰 Prerequisites

- ☕ Java 21
- 📦 Maven
- 🐳 Docker + Docker Compose

---

## 🐘 Start the database

```bash
docker compose -f db/docker-compose.yaml up -d
```

| Property | Value |
|----------|--------|
| Host | `localhost` |
| Port | `5432` |
| Database | `bookshopdb` |
| User | `bookshop` |
| Password | `bookshop` |

---

## 🧪 Run the tests

```bash
./mvnw test
```

🟢 Green = Correct  
🔴 Red = Feedback

---

# 🏁 Exercise 01 — Enable auditing for the domain

**Test class:** `example01/AuditedTest`

## 🔍 Initial state

History endpoints return **no audit entries**.

## 🎯 Goal

Enable auditing so history endpoints return audit entries.

## ❓ Open Questions

- Why does Envers not track entities automatically?
- What qualifies an entity for auditing?
- Where do audit tables come from?

## 💡 Answers

- Envers is optional to avoid storage overhead and unintended auditing of sensitive data. -> Security and Storage
- Entities must be explicitly marked/configured for auditing. Once enabled, INSERT, UPDATE, DELETE are tracked.
- Envers generates audit tables (`*_AUD`) plus a revision table automatically during schema generation.

---

# 🔐 Exercise 02 — Exclude a field from auditing

**Test class:** `example02/NotAuditedTest`

## 🎯 Goal

Exclude `internalNotes` from auditing.

## ❓ Open Questions

- Can you audit an entity but ignore specific attributes?
- What happens to schema generation when excluding a field?
- Does “not audited” mean “not stored anywhere”?

## 💡 Answers

- Yes. Individual properties can be excluded while auditing the rest of the entity.
- The excluded field will not appear in the audit table schema.
- The field remains stored in the main entity table — it is just not versioned.

---

# 👤 Exercise 03 — Store username in revision metadata

**Test class:** `example03/UsernameRevisionTest`

## 🎯 Goal

Persist `username` in revision metadata.

## ❓ Open Questions

- What is a revision entity?
- When is a revision created?
- How do you safely pass request information?

## 💡 Answers

- A revision entity stores revision metadata (revision number, timestamp, custom fields like username).
- Revisions are created when a transaction modifying audited entities is flushed/committed.
- Capture header in a request filter, store temporarily (e.g., ThreadLocal), read it in a revision listener, and clear it afterwards.

---

# 🗑 Exercise 04 — Keep entity state on delete

**Test class:** `example04/DeletionHistoryTest`

## 🎯 Goal

Store full entity state on delete.

## ❓ Open Questions

- Does Envers store delete state by default?
- Is this configuration-based?
- What are the trade-offs?

## 💡 Answers

- Not always fully — depends on configuration.
- Yes, Envers provides a configuration option to store entity data on delete.
- Trade-off: more storage usage vs. more complete historical records.

---

# 🔎 Exercise 05 — Advanced Property Queries

**Test class:** `example05/QueriesTest`

Now we introduce **property-based filtering** in audit queries.

---

## 📘 Scenario

Two books are created:

- `"Java fundamentals"` (1975)
- `"New features - Java 25"` (2025)

Both contain `"Java"` in the title.

You must implement history queries that:

1. Filter by **title substring**
2. Return audited data
3. Support ordering

---

## 🎯 Goal — Part A

Implement:

```
GET /history/book/title/{title}
```

### ✅ Acceptance Criteria

For:

```
GET /history/book/title/Java
```

- Response status is `200`
- Exactly **2 books** are returned
- Every book title contains `"Java"`

---

## 🎯 Goal — Part B

Implement:

```
GET /history/book/title/{title}/ordered
```

### ✅ Acceptance Criteria

For:

```
GET /history/book/title/Java/ordered
```

- First result has `publicationYear == 2025`
- Second result has `publicationYear == 1975`

---

## ❓ Open Questions

- How do you filter audited entities by property values?
- How do you apply ordering in an Envers audit query?
- Are you querying current state or historical snapshots?
- Should you use projection or return full entity snapshots?

---

## 💡 Answers

- Use the **Envers AuditReader query API** with `AuditEntity.property("title").like(...)`.
- Apply ordering using `addOrder(AuditEntity.property("publicationYear").desc())`.
- You are querying **historical snapshots**, not the live entity table.
- Return full entity snapshots unless projection is explicitly required.

---

# 📊 Exercise 06 — Horizontal & Vertical Queries

**Test class:** `example06/HorizontalAndVerticalQueriesTest`

---

## 🌍 Part A — Horizontal Query

**Endpoint:** `GET /history/book/timestamp/{timestamp}`

## ❓ Open Questions

- How do you map a timestamp to a revision?
- Does Envers query by revision number or date?

## 💡 Answers

- Resolve timestamp to revision number, then query entity state at that revision.
- Internally Envers queries by revision number.

---

## 📈 Part B — Vertical Query

**Endpoint:** `GET /history/author/authorName/{name}`

Order: `DEL > MOD > ADD`

## ❓ Open Questions

- How do you include revision metadata?
- Where does revision type come from?

## 💡 Answers

- Use query mode returning entity + revision entity + revision type.
- Revision type (ADD, MOD, DEL) is provided by Envers automatically.

---

# 🔎 Exercise 07 — Search history by username

**Test class:** `example07/SearchHistoryTest`

**Endpoint:** `GET /history/author/username/{username}`

## ❓ Open Questions

- How are revision and entity data joined?
- Difference between entity and revision constraints?

## 💡 Answers

- Audit tables contain a revision foreign key referencing the revision table.
- Entity constraints filter snapshot data; revision constraints filter metadata (username, timestamp, revision number).

---

# 🏆 Completion Criteria

- 🟢 All tests pass
- 🚫 No hacks
- 🧼 Clean implementation

---

## ✨ Final Thought

Envers does not just track data.  
It tracks responsibility.  
It tracks evolution.

Make it a good history.
