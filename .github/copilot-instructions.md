# Copilot Instructions

## Default Behavior

- When asked to create task, always create issues or epics (this is the plan), do NOT make local code changes.
- Only implement code changes when explicitly asked with words like "implement", "execute", "do it", or "make the changes".

## Issue Creation

- When creating issues, use the GitHub CLI (`gh issue create`) to create real issues in the repository.
- Include clear descriptions, acceptance criteria, and file references when relevant.
- an issue is always related to an epic (`addSubIssue` api graphql mutation), unless it is specified that it's not necessary
- the content must @copilot oriented
- issue is a bug or a enhancement, it must be labeled as such (ask if unsure about the label to use)
- all issues start with a Todo status for project board view
- when a sub-issue is moved to "In Progress", also move its parent epic to "In Progress"
- epic is marked "Done" only when all sub-issues are completed

## GitHub CLI Commands Reference

### Discover Project Info

```bash
# List projects to get project number and ID
gh project list --owner OWNER

# Get status field ID and options
gh project field-list PROJECT_NUMBER --owner OWNER --format json | jq '.fields[] | select(.name == "Status")'
```

### Create an Issue

```bash
gh issue create --title "Title" --label "enhancement" --body "Description with @copilot instructions"
```

### Get Issue Node ID

```bash
gh api graphql -f query='query { repository(owner: "OWNER", name: "REPO") { issue(number: NUMBER) { id } } }'
```

### Add Sub-Issue to Epic

```bash
gh api graphql -f query='mutation { addSubIssue(input: {issueId: "EPIC_NODE_ID", subIssueId: "ISSUE_NODE_ID"}) { issue { id } } }'
```

### Add Issue to Project Board

```bash
gh project item-add PROJECT_NUMBER --owner OWNER --url https://github.com/OWNER/REPO/issues/NUMBER
```

### Get Project Item ID

```bash
gh project item-list PROJECT_NUMBER --owner OWNER --format json | jq '.items[] | select(.content.number == NUMBER) | .id'
```

### Set Issue Status on Project Board

```bash
gh project item-edit --project-id PROJECT_ID --id ITEM_ID --field-id STATUS_FIELD_ID --single-select-option-id OPTION_ID
```
