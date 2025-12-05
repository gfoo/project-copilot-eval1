# Copilot Instructions

## Default Behavior

- When asked to create task, always create issues or epics (this is the plan), do NOT make local code changes.
- Only implement code changes when explicitly asked with words like "implement", "execute", "do it", or "make the changes".

## Issue Creation

- When creating issues, use the GitHub CLI (`gh issue create`) to create real issues in the repository.
- Include clear descriptions, acceptance criteria, and file references when relevant.
- an issue is always related to an epic, unless it is specified that it's not necessary
- the content must @copilot oriented
- issue is a bug or a enhancement, it must be labeled as such (ask if unsure about the label to use)
