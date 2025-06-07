import { useState, useCallback } from "react";
import {
  Notification,
  CreateNotificationDto,
  UpdateNotificationDto,
  NotificationStatus,
  getAllNotifications,
  getOwnedNotifications,
  createNotification,
  updateNotification,
} from "@/lib/api/notifications";
import { toast } from "sonner";

export const useNotifications = () => {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchAllNotifications = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getAllNotifications();
      setNotifications(data || []);
    } catch (err) {
      console.error("Failed to fetch notifications:", err);
      setError("Failed to load notifications");
      toast.error("Failed to load notifications");
      setNotifications([]);
    } finally {
      setLoading(false);
    }
  }, []);

  const fetchOwnedNotifications = useCallback(
    async (status?: NotificationStatus) => {
      try {
        setLoading(true);
        setError(null);
        const data = await getOwnedNotifications(status);
        setNotifications(data || []);
      } catch (err) {
        console.error("Failed to fetch owned notifications:", err);
        setError("Failed to load owned notifications");
        toast.error("Failed to load owned notifications");
        setNotifications([]);
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const addNotification = useCallback(
    async (notificationData: CreateNotificationDto) => {
      try {
        setLoading(true);
        setError(null);
        const newNotification = await createNotification(notificationData);

        toast.success("Notification created successfully");
        return newNotification;
      } catch (err) {
        setError("Failed to create notification");
        toast.error("Failed to create notification");
        throw err;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const updateNotificationById = useCallback(
    async (id: number, data: UpdateNotificationDto) => {
      try {
        setLoading(true);
        setError(null);
        const updatedNotification = await updateNotification(id, data);
        setNotifications((prev) =>
          prev.map((notification) =>
            notification.id === id ? updatedNotification : notification
          )
        );
        toast.success("Marked as read");
        return updatedNotification;
      } catch (err) {
        setError("Failed to update notification");
        toast.error("Failed to update notification");
        throw err;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const markAsRead = useCallback(
    async (id: number) => {
      try {
        const notification = notifications.find((n) => n.id === id);
        if (!notification) return;

        await updateNotificationById(id, {
          userId: notification.userId,
          message: notification.message,
          status: "READ",
        });
      } catch (error) {
        console.error("Failed to mark notification as read:", error);
        toast.error("Failed to mark notification as read");
      }
    },
    [updateNotificationById, notifications]
  );

  const markAllAsRead = useCallback(async () => {
    try {
      setLoading(true);
      const promises = notifications
        .filter((notification) => notification.status === "UNREAD")
        .map((notification) =>
          updateNotificationById(notification.id, {
            userId: notification.userId,
            message: notification.message,
            status: "READ",
          })
        );
      await Promise.all(promises);
      toast.success("All notifications marked as read");
    } catch (error) {
      console.error("Failed to mark all notifications as read:", error);
      toast.error("Failed to mark all notifications as read");
    } finally {
      setLoading(false);
    }
  }, [notifications, updateNotificationById]);

  return {
    notifications,
    loading,
    error,
    fetchAllNotifications,
    fetchOwnedNotifications,
    addNotification,
    updateNotificationById,
    markAsRead,
    markAllAsRead,
  };
};
