# 20 Docker

# Question 1: What is Docker and how does it differ from a Virtual Machine (VM)?

## Answer
Docker is a platform for developing, shipping, and running applications in isolated environments called containers. 
- **Containers vs VMs**: A VM virtualizes the hardware and requires a full guest operating system. Docker containers virtualize the operating system. They share the host OS kernel, making them lightweight, fast to start, and consuming significantly fewer resources than VMs.

# Question 2: What is a Dockerfile, and what are its most common instructions? Explain FROM, RUN, CMD, and ENTRYPOINT.

## Answer
A Dockerfile is a text document containing all the commands a user could call on the command line to assemble an image.
- **FROM**: Initializes a new build stage and sets the base image for subsequent instructions (e.g., `FROM openjdk:17-alpine`).
- **RUN**: Executes commands in a new layer on top of the current image and commits the results, usually for installing packages.
- **CMD**: Provides defaults for an executing container. There can only be one `CMD` instruction. If a user specifies arguments to `docker run`, they override the default `CMD`.
- **ENTRYPOINT**: Allows you to configure a container that will run as an executable. Command-line arguments to `docker run` are appended to the `ENTRYPOINT`.

# Question 3: Explain the difference between CMD and ENTRYPOINT in a Dockerfile.

## Answer
Both define what command runs when a container starts, but they behave differently when you pass arguments via `docker run`:
- **ENTRYPOINT** specifies the main executable that should always run. It cannot be easily overridden (requires `--entrypoint` flag). Any arguments passed in `docker run` are appended to the ENTRYPOINT command.
- **CMD** sets the default arguments or the default command. If you pass arguments in `docker run`, the `CMD` instruction is completely ignored and overridden.
- **Best Practice**: Use `ENTRYPOINT` for the core application executable, and `CMD` for default flags/arguments.

# Question 4: What is the difference between a Docker image and a Docker container?

## Answer
- **Image**: An immutable, read-only template with instructions for creating a Docker container. It contains the application code, runtime, libraries, environment variables, and config files.
- **Container**: A runnable, ephemeral instance of an image. You can start, stop, move, or delete a container. While an image is the "blueprint", the container is the "house" built from it.

# Question 5: How does Docker handle storage? What is the difference between bind mounts and volumes?

## Answer
By default, files created inside a container are stored on a writable container layer and are lost when the container stops. Docker provides mechanisms for persistent data:
- **Volumes**: Managed entirely by Docker (stored in `/var/lib/docker/volumes/` on Linux). They are the preferred mechanism for persisting data because they are decoupled from the host filesystem structure and are easier to back up and share.
- **Bind Mounts**: Map a specific file or directory on the host machine to a directory in the container. They rely on the host machine's filesystem directory structure. Useful for local development (e.g., mounting source code).

# Question 6: What is Docker Compose, and in what scenarios would you use it?

## Answer
Docker Compose is a tool for defining and running multi-container Docker applications using a YAML file (`docker-compose.yml`).
- **Scenarios**: It is heavily used in local development, CI/CD, and staging environments to spin up an application's entire stack (e.g., frontend, backend, database, cache) with a single command (`docker-compose up`).

# Question 7: What strategies can you use to reduce the size of a Docker image?

## Answer
- **Multi-stage builds**: Use a larger base image (like full JDK) to compile the app, and a minimal base image (like JRE or distroless) for the final runtime stage, discarding the build tools.
- **Minimal Base Images**: Use `alpine` or `distroless` images instead of full OS images like `ubuntu`.
- **Combine RUN instructions**: Chain commands with `&&` to reduce the number of image layers.
- **.dockerignore file**: Exclude unnecessary files (e.g., `.git`, `target/`, IDE configs) from being copied into the build context.

# Question 8: Explain the different Docker networking models, specifically bridge, host, and none.

## Answer
- **Bridge (default)**: Creates an internal private network. Containers on the same bridge network can communicate with each other via IP or DNS, but are isolated from external networks unless ports are explicitly published (`-p`).
- **Host**: Removes network isolation between the container and the Docker host. The container uses the host's networking stack directly (e.g., port 80 in the container is port 80 on the host).
- **None**: Completely disables networking for the container. It has no external or internal network interfaces (except loopback). Useful for highly secure, isolated batch jobs.

# Question 9: What is a .dockerignore file, and why is it important?

## Answer
A `.dockerignore` file works similarly to `.gitignore`. It tells the Docker CLI which files and directories to exclude when sending the build context to the Docker daemon.
- **Importance**: It significantly speeds up the build process by preventing large, unnecessary files (like `node_modules/`, `target/`, or local logs) from being uploaded. It also improves security by preventing sensitive local files from accidentally being copied into the image.

# Question 10: How do you securely pass environment variables to a Docker container at runtime?

## Answer
Environment variables shouldn't be hardcoded in the Dockerfile. Instead, pass them at runtime:
- Use the `-e` flag: `docker run -e DB_PASSWORD=secret my-app`
- Use an env file: `docker run --env-file ./env.list my-app`
- In Docker Compose, define them under `environment:` or `env_file:`.
- **For Secrets**: In production environments (like Docker Swarm or Kubernetes), use native secret management tools (Docker Secrets, k8s Secrets) rather than plain environment variables to prevent credentials from appearing in `docker inspect` logs.
