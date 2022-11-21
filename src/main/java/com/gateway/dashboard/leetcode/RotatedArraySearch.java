package com.gateway.dashboard.leetcode;

/**
 * 33. Search in Rotated Sorted Array - Medium
 * https://leetcode.com/problems/search-in-rotated-sorted-array/description/
 * There is an integer array nums sorted in ascending order (with distinct values).
 * Prior to being passed to your function, nums is possibly rotated at an unknown pivot index k (1 <= k < nums.length) such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]] (0-indexed). For example, [0,1,2,4,5,6,7] might be rotated at pivot index 3 and become [4,5,6,7,0,1,2].
 * Given the array nums after the possible rotation and an integer target, return the index of target if it is in nums, or -1 if it is not in nums.
 * You must write an algorithm with O(log n) runtime complexity.
 * Constraints:
 *    1 <= nums.length <= 5000
 *    -104 <= nums[i] <= 104
 *    All values of nums are unique.
 *    nums is an ascending array that is possibly rotated.
 *    -104 <= target <= 104
 */
public class RotatedArraySearch {

    // If nums[mid] and target are "on the same side" of nums[0], we just take nums[mid].
    // Otherwise, we use -infinity or +infinity as needed.
    public int search(int[] nums, int target) {
        if(nums == null||nums.length == 0) return -1;
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            // target and mid are on the same side
            if((nums[mid]-nums[nums.length-1])*(target-nums[nums.length-1])>0) {
                if(nums[mid] < target) lo = mid + 1;
                else hi = mid;
            } else if(target > nums[nums.length-1])
                hi = mid; // target on the left side
            else
                lo = mid + 1; // target on the right side
        }
        return nums[lo] == target ? lo : -1; // hi == lo
    }

}
