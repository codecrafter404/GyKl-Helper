# VPlan Discord Bot
A Discord Bot that announces the current VPlan.

# Environments
This Discord Bot is Based on Docker und uses Environment variables.
| Environment | Description | Default | Type |
| --- | --- | --- | --- |
| VPLAN_ANNOUNCE | The Announcment Channel | vertretungsplan | String |
| VPLAN_BOT | The Bot Channel | bot | String |
| VPLAN_HOST | The host for the API | --- | String |
| VPLAN_PASSWORD | The Password for the API | --- | String |
| VPLAN_TIME | The hour for the announcment| 16 | Int(0-24)
| VPLAN_CLASS | The Class for the VPlan | 7b | String |
| VPLAN_DEBUG | Enable Debug | false | String |

# Volumes
The Cointainer need a mounted JSON configfile within the discord access token.
| Mount | Description |
| --- | --- |
| /config/secret.json | Includes the discord access token |

# Example secret.json File
```
{
    "token": "TOP Secret Token, not shared with Everyone!"
}
```

# WARRENTY
NO WARRENTY, THIS IS AN UNOFFICAL PROJECT!
