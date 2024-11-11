<!-- Listen, I know align=center is basically deprecated, but it's GitHub. You use what you can, because there's not a lot of that going around. -->
<div align="center">
	<img src="./public/logo.svg" alt="Logo" width="80" height="80">
	<h3 align="center">MOB3000 Group 11</h3>
	<p align="center">
		A chess training app made with Kotlin.
		<br><br>
		<a href="">View Demo</a>
		◈
		<a href="https://github.com/frigvid/app2000-gruppe11/issues/new?assignees=&labels=bug&projects=&template=bug-report.yml&title=bug%3A+">Report Bug</a>
		◈
		<a href="https://github.com/frigvid/app2000-gruppe11/issues/new?assignees=&labels=needs+triage%2Cenhancement&projects=&template=feature-request.yml&title=feature%3A+">Request Feature</a>
		<br><br>
		<a href="docs/README.md"><strong>Explore the docs »</strong></a>
	</p>
</div>

<details>
<summary>Table of Contents</summary>

* [About The Project](#about-the-project)
	* [Built With](#built-with)
* [Getting Started](#getting-started)
	* [Prerequisites](#prerequisites)
	* [Installation](#installation)
* [Usage](#usage)
* [Roadmap](#roadmap)
* [License](#license)

</details>
<br>

# About the project

This project was made in conjunction with the University of South-Eastern Norway, at campus Bø, as part the course MOB3000.

You can check out the web-application this mobile application is based on [[HERE](https://github.com/frigvid/app2000-gruppe11)].

# Getting started
## Built with

The project is developed using:

- Kotlin.
- Jetpack Compose.
- [Supabase-kt](https://github.com/supabase-community/supabase-kt).
- [Chesslib](https://github.com/bhlangonijr/chesslib).

## Prerequisites
### Database

The database used for this project is PostgreSQL via Supabase. It's using the same database as the mobile application. The pre-requisites for the database are thus the same, and can be found in the [`PREREQUISITES.sql`](https://github.com/frigvid/app2000-gruppe11/blob/master/PREREQUISITES.sql) file in the older repository.

### Environment file

This project requires a connection to Supabase to compile. Specifically, this means you'll need a few key-value pairs.

You'll need to create a `.env.local` file in the root repository with the following keys. And values, of course.

```text
NEXT_PUBLIC_SUPABASE_URL=
NEXT_PUBLIC_SUPABASE_ANON_KEY=
```

## Installation

Installation is as simple as:

1. Run `gradle sync`.
2. Then run `gradle build`.

If any problems occur, you can try a `gradle sync` too.

# License

The repository `LICENSE` file, as you may have noticed, marks this repository as MIT. However, due to the nature of its contents, it's actually multi-licensed. Unless otherwise specified, the licenses below apply to their respective areas:

- Code: `MIT`
- Documentation: `CC0`
- Resources (e.g. images): `CC0`

As mentioned, this is not withstanding resources that have other licenses.

