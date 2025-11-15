### Admin Portal
- Implement the admin portal for managing users, roles, and permissions.

### Bank Only Back-End
- Develop the back-end functionality for bank-related operations.
- Ensure secure communication between the application and the bank.

### SendToBank -> Self OR Anyone
- Add functionality to allow users to send transactions to their own accounts or to others.
- Include proper validation and authorization checks.

### Add from Bank
- Implement the feature to allow users to add funds from their bank accounts.
- Integrate with the bank's API for secure fund transfers.

### UI Adjust Accordingly
- Update the user interface to reflect the new features.
- Ensure the UI is user-friendly and responsive.

### Pipeline Build
- Set up a CI/CD pipeline to automate the build process.
- Ensure the pipeline includes steps for testing, building, and packaging the application.

### Dockerized
- Create a Dockerfile to containerize the application.
- Ensure the Docker image includes all necessary dependencies and configurations.
- Push the Docker image to a container registry (e.g., Docker Hub, AWS ECR, or Azure Container Registry).

### Deploy using K8s
- Write Kubernetes manifests (YAML files) for deploying the application.
- Include resources such as Deployments, Services, and ConfigMaps.
- Use a Kubernetes cluster to deploy and manage the application.
- Ensure proper scaling, load balancing, and monitoring configurations.